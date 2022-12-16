package dash;

import dash.internal.event.DashEventBus;
import dash.internal.event.channels.AcceptingChannel;
import dash.internal.util.Threads;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@State(Scope.Benchmark)
public class BenchmarkEventChannel {
    private final ExecutorService asyncExec = Executors.newFixedThreadPool(4);
    private ScheduledExecutorService simpleScheduler = Executors.newSingleThreadScheduledExecutor();
    private static final TestEvent EVENT = new TestEvent();

    //@Param({"MONITOR", "MAIN", "ASYNC"})
    @SuppressWarnings("unused")
    private ScheduleType type = ScheduleType.MONITOR;
    private IEventBus bus;

    @Setup
    public void setup() {
        Threads.primaryThread = Thread.currentThread();
        var random = new Random(0);
        bus = new DashEventBus(asyncExec, simpleScheduler);
        for (int i = 0; i < 1000; i++) {
            var channel = new AcceptingChannel<>(
                    type,
                    null,
                    random.nextInt(100),
                    null,
                    bus
            ).subscribeAlways((b, c) -> {
                //    latch.countDown();
            });
        }
    }

    @Benchmark
    public void broadcastMessages() throws InterruptedException {
        bus.postEvent(EVENT, whenDone -> {
        });
        //  latch.await();
    }

    @TearDown
    public void cleanUp() {
        simpleScheduler.shutdownNow();
        asyncExec.shutdownNow();
        bus = new DashEventBus(asyncExec, simpleScheduler);

    }
}
