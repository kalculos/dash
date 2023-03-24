/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        bus = new DashEventBus(simpleScheduler,asyncExec);
        for (int i = 0; i < 1000; i++) {
            var channel = new AcceptingChannel<>(
                    type,
                    null,
                    random.nextInt(100),
                    null,
                    bus
            ).subscribeAlways((b, c) -> b.fireNext());
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
       // bus = new DashEventBus(asyncExec, simpleScheduler);

    }
}
