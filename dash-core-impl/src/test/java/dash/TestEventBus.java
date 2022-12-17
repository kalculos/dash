package dash;

import dash.internal.event.DashEventBus;
import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEventBus {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    IEventBus bus;
    static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    static volatile boolean stop = false;

    @AfterAll
    public static void teardown() {
        stop = true;
    }

    @Test
    public void testEventDelivery() {
        bus = new DashEventBus(executor, scheduler);
        var inte = new AtomicBoolean(false);
        var channel = new AcceptingChannel<>(
                ScheduleType.MONITOR,
                null,
                0,
                null,
                bus
        );
        channel.subscribeOnce((e, c) -> inte.set(((TestEvent) e).value == 0));
        bus.postEvent(new TestEvent(0), event -> {
        });
        assertTrue(inte.get());
    }

    @Test
    public void testEventDeliveryPriority() throws InterruptedException {
        bus = new DashEventBus(executor, scheduler);
        var c1 = new AcceptingChannel<>(
                ScheduleType.MAIN,
                null,
                0,
                null,
                bus
        );
        var c2 = new AcceptingChannel<>(
                ScheduleType.MAIN,
                null,
                1,
                null,
                bus
        );
        var c2s = new AtomicLong();
        var c1s = new AtomicLong();
        c2.subscribeAlways((e, c) -> c2s.set(System.nanoTime()));
        c1.subscribeAlways((e, c) -> c1s.set(System.nanoTime()));
        bus.postEvent(new TestEvent(0), event -> {
        });
        Thread.sleep(100);
        assertTrue(c1s.get() < c2s.get());
    }

    @Test
    public void testEventDeliveryPriorityByScheduleType() throws InterruptedException {
        bus = new DashEventBus(executor, scheduler);
        var c1 = new AcceptingChannel<>(
                ScheduleType.ASYNC,
                null,
                0,
                null,
                bus
        );
        var c2 = new AcceptingChannel<>(
                ScheduleType.MAIN,
                null,
                1,
                null,
                bus
        );
        var c2s = new AtomicLong();
        var c1s = new AtomicLong();
        c2.subscribeAlways((e, c) -> c2s.set(System.nanoTime()));
        c1.subscribeAlways((e, c) -> c1s.set(System.nanoTime()));
        bus.postEvent(new TestEvent(0), event -> {
        });
        Thread.sleep(100);
        assertTrue(c1s.get() > c2s.get());
    }

    @Test
    public void testEventDispatchAsync() throws InterruptedException {
        bus = new DashEventBus(executor, scheduler);
        var tid = new AtomicLong(-9);
        new AcceptingChannel<>(
                ScheduleType.ASYNC,
                null,
                0,
                null,
                bus
        ).subscribeOnce((a, b) -> {
            tid.set(Thread.currentThread().getId());
        });
        bus.postEvent(new TestEvent(0), event -> {
        });
        Thread.sleep(100);
        assertTrue(tid.get() != -9, "Async Handler doesn't dispatched");
        assertTrue(tid.get() != Thread.currentThread().getId(), "Async Handler doesn't go async");
    }

    @Test
    public void testPublishEventInHandler() throws InterruptedException {
        bus = new DashEventBus(executor, scheduler);
        new AcceptingChannel<>(
                ScheduleType.MAIN,
                null,
                0,
                null,
                bus
        ).filterForType(TestEvent.class).subscribeAlways((a, b) -> {
            b.getBus().postEvent(new TestEvent2(1), e -> {
            });
        });
        AtomicBoolean bool = new AtomicBoolean(false);
        new AcceptingChannel<>(ScheduleType.MAIN, null, 0, null, bus)
                .filterForType(TestEvent2.class)
                .subscribeAlways((a, b) -> {
                    bool.set(true);
                });
        bus.postEvent(new TestEvent(0), evt -> {
        });
        Thread.sleep(100);
        assertTrue(bool.get());
    }

    @RequiredArgsConstructor
    static class TestEvent2 extends AbstractEvent {
        private final int value;
    }

    @RequiredArgsConstructor
    class TestEvent extends AbstractEvent {
        private final int value;
    }
}
