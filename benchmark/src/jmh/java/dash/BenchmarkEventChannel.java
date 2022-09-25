package dash;

import dash.internal.event.DashEventBus;
import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@State(Scope.Benchmark)
public class BenchmarkEventChannel {
    private IEventBus bus = new DashEventBus(Executors.newFixedThreadPool(4), Executors.newSingleThreadExecutor());

    @Param({"MONITOR", "MAIN", "ASYNC"})
    private ScheduleType type;
    private CountDownLatch latch;

    @Setup
    public void setup() {
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
}
