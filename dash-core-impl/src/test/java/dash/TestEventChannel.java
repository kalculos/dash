package dash;

import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestEventChannel {
    IEventChannel<AbstractEvent> channel = new AcceptingChannel<>(
            ScheduleType.MAIN,
            null,
            0,
            null,
            null
    );

    @Test
    public void testEventChannelAccepting() {
        assertNotNull(channel.apply(new TestEvent2(1)));
    }

    @Test
    public void testEventChannelFilter() {
        var c = channel.filter(it -> it instanceof TestEvent);
        assertNotNull(c.apply(new TestEvent(0)));
        assertNull(c.apply(new TestEvent2(2)));
    }

    @Test
    public void testEventChannelMapper() {
        var c = channel.map(it -> (TestEvent) it)
                .map(it -> new TestEvent2(it.value));
        var result = c.apply(new TestEvent(0));
        assertTrue(result instanceof TestEvent2);
        assertEquals(0, ((TestEvent2) result).value);
    }

    @Test
    public void testEventChannelFilterAndMap() {
        var c = channel.filterForType(TestEvent.class);
        assertNull(c.apply(new TestEvent2(0)));
        assertNotNull(c.apply(new TestEvent(0)));
    }

    @RequiredArgsConstructor
    class TestEvent extends AbstractEvent {
        private final int value;
    }

    @RequiredArgsConstructor
    class TestEvent2 extends AbstractEvent {
        private final int value;
    }
}
