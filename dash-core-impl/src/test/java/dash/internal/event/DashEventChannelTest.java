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
        bus.postEvent(new TestEventA(0), it -> {
        });
        await("test subscribeAlways #1").atMost(ofSeconds(1)).until(() -> result[0]);
        result[0] = false;
        bus.postEvent(new TestEventA(0), it -> {
        });
        await("test subscribeAlways #2").atMost(ofSeconds(1)).until(() -> result[0]);
    }

    @Test
    public void testSubscribeOnce(){
        var result = new boolean[1];
        factory.forMonitor().subscribeOnce((p,a)->result[0]=true);
        forceSleep(1);
        bus.postEvent(new TestEventA(0),it->{});
        await("test subscribeOnce #1").atMost(ofSeconds(1)).until(()->result[0]);
        result[0]=false;
        bus.postEvent(new TestEventA(0),it->{});
        await("test subscribeOnce #2").atMost(ofSeconds(1)).until(()->!result[0]);
    }
}
