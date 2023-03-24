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

package dash.internal.event;

import dash.test.event.CancellableEvent;
import dash.test.event.TestEventA;
import dash.test.event.TestEventB;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.bus.IEventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static dash.test.SharedResources.asyncPool;
import static dash.test.SharedResources.mainLoop;
import static dash.test.util.Utility.forceSleep;
import static dash.test.util.Utility.ofAdapter;
import static java.lang.System.nanoTime;
import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashEventBusTest {
    private static IEventBus bus;
    private static IEventChannelFactory channelFactory;

    @BeforeEach
    public void initEventBus() {
        bus = new DashEventBus(mainLoop, asyncPool);
        channelFactory = new SimpleEventChannelFactory(bus);
    }

    @Test
    public void testRegisterMonitor() {
        var monitorChannel = channelFactory.forMonitor("test#1", 0);
        boolean[] result = new boolean[1];
        monitorChannel.subscribeAlways((pipe, event) -> {
            result[0] = true;
        });
        // This is not the primary thread so the eventbus do submit a task to the loop.
        forceSleep(1);
        // post event
        bus.postEvent(new TestEventA(0));
        assertTrue(result[0], "Channel registration with scheduleType MONITOR");
    }

    @Test
    public void testRegisterMainAndAsync() {
        var mainChannel = channelFactory.forMain("test#main#2", 0);
        long[] result = new long[2];
        mainChannel.subscribeAlways((pipe, event) -> {
            result[0] = nanoTime();
        });
        var asyncChannel = channelFactory.forAsync("test#async#2", 0);
        asyncChannel.subscribeAlways((pipe, evt) -> {
            result[1] = nanoTime();
        });
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("Test async & main delivery and priority").atMost(ofSeconds(3)).until(() -> result[1] > result[0]);
    }

    @Test
    public void testRegisterInHandler() {
        var mainChannel = channelFactory.forMain();
        var result = new boolean[1];
        mainChannel.subscribeAlways((pipe, evt) -> {
            pipe.channel().subscribeOnce((a, b) -> result[0] = true);
            pipe.fireNext();
        });
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("subhandler is registered").atMost(ofSeconds(1)).until(() -> result[0]);
    }

    @Test
    public void testPipelineUnsubscribe() {
        var mainChannel = channelFactory.forMain();
        AtomicInteger result = new AtomicInteger(0);
        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> {
                    result.getAndIncrement();
                    pipe.unsubscribe();
                });
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        bus.postEvent(new TestEventA(0));
        await("pipeline is unsubscribed").atMost(ofSeconds(1)).until(() -> result.get() == 1);
    }

    @Test
    public void testPipelineWithoutFireNext() {
        var mainChannel = channelFactory.forMain();
        var result = new AtomicInteger(0);
        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> result.getAndIncrement());
        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> result.getAndIncrement());
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("pipeline is not fired").atMost(ofSeconds(1)).until(() -> result.get() == 1);
    }

    @Test
    public void testPipelineWithFireNext() {
        var mainChannel = channelFactory.forMain();
        var result = new AtomicInteger(0);
        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> {
                    result.getAndIncrement();
                    pipe.fireNext();
                });
        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> result.getAndIncrement());
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("pipeline is fired").atMost(ofSeconds(1)).until(() -> result.get() == 2);
    }

    @Test
    public void testPipelineFireNextException() {
        var mainChannel = channelFactory.forMain();
        AtomicInteger result = new AtomicInteger(0);

        mainChannel
                .filterForType(TestEventA.class)
                .subscribeAlways((pipe, evt) -> {
                    try {
                        pipe.unsubscribe();
                        pipe.unsubscribe();
                    } catch (IllegalStateException e) {
                        result.getAndIncrement();
                    }
                });

        forceSleep(1);
        bus.postEvent(new TestEventA(0));

        await("test pipeline exception").atMost(ofSeconds(1)).until(() -> result.get() == 1);
    }

    @Test
    public void testPublishInHandler() {
        var mainChannel = channelFactory.forMain();
        var result = new boolean[1];
        mainChannel.subscribeOnce((a, b) -> {
            a.channel().getBus().postEvent(new TestEventB(0));
        });
        mainChannel.filterForType(TestEventB.class).subscribeOnce((a, b) -> result[0] = true);
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("publish in handler").atMost(ofSeconds(1)).until(() -> result[0]);
    }

    @Test
    public void testPostCallback() {
        var succeed = new boolean[1];
        bus.postEvent(new TestEventA(0)).onComplete(it -> succeed[0] = true);
        await("Test eventbus callback").atMost(ofSeconds(1)).until(() -> succeed[0]);
    }

    @Test
    public void testChannelPriority() { // note: this also tests EventPipeline#fireNext
        var time = new long[2];
        var earlyChannel = channelFactory.forMonitor("early", 1);
        var laterChannel = channelFactory.forMonitor("later", 2);
        laterChannel.subscribeAlways(ofAdapter(evt -> time[0] = nanoTime()));
        earlyChannel.subscribeAlways(ofAdapter(evt -> time[1] = nanoTime()));
        forceSleep(2);
        bus.postEvent(new TestEventA(0));
        await("Test event channel priority").atMost(ofSeconds(2)).until(() -> time[0] > time[1]);
    }

    @Test
    public void testSchedulePriority() {
        var time = new long[3];
        channelFactory.forMonitor("earliest", 0)
                .subscribeAlways(ofAdapter(abstractEvent -> time[0] = nanoTime()));
        channelFactory.forMain("normal", 0)
                .subscribeAlways(ofAdapter(abstractEvent -> time[1] = nanoTime()));
        channelFactory.forAsync("latest", 0)
                .subscribeAlways(ofAdapter(abstractEvent -> time[2] = nanoTime()));
        forceSleep(2);
        bus.postEvent(new TestEventA(0));
        forceSleep(2);
        System.out.println(Arrays.toString(time));
        assertTrue(time[0] < time[1], "MONITOR < MAIN");
        assertTrue(time[1] < time[2], "MAIN < ASYNC");
    }

    @Test
    public void testIgnoreCancelled() {
        var result = new boolean[1];
        channelFactory.forMonitor().filterForType(CancellableEvent.class).subscribe(false, (a, b) -> b.setCancelled(true));
        channelFactory.forMonitor().subscribe(true, (a, b) -> result[0] = true);
        forceSleep(1);
        bus.postEvent(new CancellableEvent());
        await().atMost(ofSeconds(1)).until(() -> !result[0]);
    }

    @Test
    public void testAsyncUnsubscribe() {
        var result = new boolean[2];
        channelFactory.forAsync()
                .subscribeOnce((a, b) -> {
                    result[0] = true;
                }).subscribeAlways((a, b) -> {
                    result[1] = true;
                });
        forceSleep(1);
        bus.postEvent(new CancellableEvent());
        await().atMost(ofSeconds(1)).until(() -> !result[0] && result[1]);
    }
}