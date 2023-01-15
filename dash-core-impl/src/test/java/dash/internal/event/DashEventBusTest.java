package dash.internal.event;

import dash.test.event.TestEventA;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.bus.IEventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        bus.postEvent(new TestEventA(0), it -> {
        });
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
        bus.postEvent(new TestEventA(0), it -> {
        });
        await("Test async & main delivery and priority").atMost(ofSeconds(3)).until(() -> result[1] > result[0]);
    }

    @Test
    public void testPostCallback() {
        var succeed = new boolean[1];
        bus.postEvent(new TestEventA(0), it -> succeed[0] = true);
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
        bus.postEvent(new TestEventA(0), it -> {
        });
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
        bus.postEvent(new TestEventA(0), it -> {
        });
        forceSleep(2);
        System.out.println(Arrays.toString(time));
        assertTrue(time[0] < time[1],"MONITOR < MAIN");
        assertTrue(time[1]<time[2], "MAIN < ASYNC");
    }
}