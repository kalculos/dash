package dash;

import dash.internal.event.DashEventBus;
import dash.internal.event.channels.AcceptingChannel;
import dash.internal.scheduler.SimpleCatchingScheduler;
import dash.internal.util.Threads;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.scheduler.Scheduler;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@State(Scope.Benchmark)
public class BenchmarkEventChannel {
    private Scheduler simpleScheduler = new SimpleCatchingScheduler(5);
    private IEventBus bus = new DashEventBus(Executors.newFixedThreadPool(4), simpleScheduler);

    @Param({"MONITOR", "MAIN", "ASYNC"})
    private ScheduleType type;
    private CountDownLatch latch;
    private Thread thread;
    private volatile boolean stop = false;

    @Setup
    public void setup() {
        thread = new Thread(() -> {
            Threads.primaryThread = Thread.currentThread();
            while (!stop) {
                simpleScheduler.tick();
            }
        });
        thread.start();
        var random = new Random(0);
        latch = new CountDownLatch(1000);
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
        bus.postEvent(new TestEvent());
        //  latch.await();
    }

    @TearDown
    public void cleanUp() {
        stop = true;

    }
}
