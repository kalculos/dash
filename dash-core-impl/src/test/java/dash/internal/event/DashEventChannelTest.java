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

import dash.internal.event.channels.AcceptingChannel;
import dash.test.event.TestEventA;
import dash.test.event.TestEventB;
import dash.test.event.WrapEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dash.test.SharedResources.asyncPool;
import static dash.test.SharedResources.mainLoop;
import static dash.test.util.Utility.forceSleep;
import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class DashEventChannelTest {
    private static final IEventChannel<?> ROOT = new AcceptingChannel<>(
            ScheduleType.MAIN,
            null, 0,
            null,
            null
    );
    private static IEventChannelFactory factory;
    private static IEventBus bus;

    @BeforeEach
    public void setup() {
        bus = new DashEventBus(mainLoop, asyncPool);
        factory = new SimpleEventChannelFactory(bus);
    }

    @Test
    public void testFilter() {
        var channel = ROOT.filter(it -> it instanceof TestEventA);
        assertNull(channel.apply(new TestEventB(0)));
        assertNotNull(channel.apply(new TestEventA(0)));
    }

    @Test
    public void testMapper() {
        var channel = ROOT.map(WrapEvent::new);
        assertTrue(channel.apply(new TestEventA(0)) instanceof WrapEvent i && i.getAnotherEvent() instanceof TestEventA);
    }

    @Test
    public void testFilterForType() {
        var channel = ROOT.filterForType(TestEventB.class);
        assertNull(channel.apply(new TestEventA(0)));
        assertTrue(channel.apply(new TestEventB(3)) instanceof TestEventB testEventB && testEventB.getI() == 3);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testChannelComparable() {
        var channelA = new AcceptingChannel(
                ScheduleType.MAIN,
                null, 1,
                null,
                null
        );
        assertTrue(channelA.compareTo(ROOT) > 0);
    }

    @Test
    public void testSubscribeAlways() {
        var result = new boolean[1];
        factory.forMonitor().subscribeAlways((p, a) -> result[0] = true);
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("test subscribeAlways #1").atMost(ofSeconds(1)).until(() -> result[0]);
        result[0] = false;
        bus.postEvent(new TestEventA(0));
        await("test subscribeAlways #2").atMost(ofSeconds(1)).until(() -> result[0]);
    }

    @Test
    public void testSubscribeOnce(){
        var result = new boolean[1];
        factory.forMonitor().subscribeOnce((p,a)->result[0]=true);
        forceSleep(1);
        bus.postEvent(new TestEventA(0));
        await("test subscribeOnce #1").atMost(ofSeconds(1)).until(()->result[0]);
        result[0]=false;
        bus.postEvent(new TestEventA(0));
        await("test subscribeOnce #2").atMost(ofSeconds(1)).until(()->!result[0]);
    }
}
