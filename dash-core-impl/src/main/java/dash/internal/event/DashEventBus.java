package dash.internal.event;

import dash.internal.util.SortedArrayList;
import dash.internal.util.Threads;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.HandleResult;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import static io.ib67.dash.event.ScheduleType.*;
import static java.util.Objects.requireNonNull;

public class DashEventBus implements IEventBus {
    private static final int MAX_WAIT_TIME = Integer.getInteger("dash.eventbus.handlerList.maxWaitSecond", 5);
    private final Map<ScheduleType, List<RegisteredHandler<?>>> handlers = new EnumMap<>(ScheduleType.class);

    private final ExecutorService asyncExecutor;

    private final ScheduledExecutorService mainExecutor;

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock registerWriteLock = lock.writeLock();
    private final Lock registerReadLock = lock.readLock();

    private final Queue<Pair<IEventChannel<?>, IEventHandler<?>>> registerQueue
            = new ArrayBlockingQueue<>(Integer.getInteger("dash.eventbus.pendingListenerQueue.capacity", 32));

    public DashEventBus(ExecutorService asyncExecutor, ScheduledExecutorService mainExecutor) {
        requireNonNull(this.asyncExecutor = asyncExecutor);
        requireNonNull(this.mainExecutor = mainExecutor);
        mainExecutor.execute(() -> Threads.primaryThread = Thread.currentThread());
    }


    @Nullable
    @SuppressWarnings("unchecked")
    private static <E extends AbstractEvent> HandleResult getHandleResult(E event, RegisteredHandler<?> it) {
        var evt = it.channel().apply(event);
        if (evt == null) return null;
        return ((IEventHandler<AbstractEvent>) it.handler()).handleMessage(evt, (IEventChannel<AbstractEvent>) it.channel());
    }

    @Override
    public <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler) {
        requireNonNull(channel.getScheduleType());
        boolean locked = false;
        try {
            locked = registerWriteLock.tryLock();
            if (!locked) {
                registerQueue.add(Kiwi.pairOf(channel, handler));
                return;
            }
            register0(channel, handler);
        } finally {
            if (locked) registerWriteLock.unlock();
        }
    }

    private <E extends AbstractEvent> void register0(IEventChannel<E> channel, IEventHandler<E> handler) {
        handlers.computeIfAbsent(channel.getScheduleType(), k -> new SortedArrayList<>())
                .add(new RegisteredHandler<>(channel, handler));
    }

    public <E extends AbstractEvent> void postEvent(E event, Consumer<E> whenDone) {
        boolean locked = false;
        try {
            locked = registerReadLock.tryLock(MAX_WAIT_TIME, TimeUnit.SECONDS);
            postEvent1(event, MONITOR);
            if (!Threads.isPrimaryThread()) {
                // don't post if nothing subscribe on main
                if (handlers.containsKey(MAIN) || handlers.containsKey(ASYNC)) {
                    mainExecutor.submit(() -> postEvent0(event, whenDone));
                }
            } else {
                postEvent0(event, whenDone);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Can't hold readLock for " + MAX_WAIT_TIME + " seconds, holding "
                    + handlers.values().stream().map(List::size).reduce(Integer::sum) + " handlers.", e);
        } finally {
            if (locked) {
                registerReadLock.unlock();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends AbstractEvent> void postEvent0(E event, Consumer<E> whenDone) {
        boolean locked = false;
        try {
            locked = registerReadLock.tryLock(MAX_WAIT_TIME, TimeUnit.SECONDS);
            postEvent1(event, MAIN);
            postEvent1(event, ASYNC);
            whenDone.accept(event);
            registerReadLock.unlock();
            locked = false;
            while (registerQueue.peek() != null) {
                // here we don't care its type.
                Pair<IEventChannel<E>, IEventHandler<E>> pair = (Pair<IEventChannel<E>, IEventHandler<E>>) (Object) registerQueue.poll();
                register0(pair.left, pair.right); //todo: test
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Can't hold readLock for " + MAX_WAIT_TIME + " seconds, holding "
                    + handlers.values().stream().map(List::size).reduce(Integer::sum) + " handlers.", e);
        } finally {
            if (locked) {
                registerReadLock.unlock();
            }
        }
    }

    private <E extends AbstractEvent> void postEvent1(E event, ScheduleType type) {
        var _handlers = handlers.getOrDefault(type, Collections.emptyList());
        var iter = _handlers.iterator();
        while (iter.hasNext()) {
            var it = iter.next();
            if (type == ASYNC) {
                asyncExecutor.submit(() -> getHandleResult(event, it));
                continue;
            }
            var result = getHandleResult(event, it);
            if (result == null) continue;
            switch (result) {
                case CONTINUE -> {
                    // do nothing.
                }
                case CANCELLED -> {
                    return;
                }
                case UNSUBSCRIBE -> iter.remove();
                default -> {
                }
            }
        }
    }
}
