package dash.internal.registry;

import dash.internal.event.DashEventBus;
import dash.internal.event.SimpleEventChannelFactory;
import dash.test.MockBot;
import dash.test.SharedResources;
import dash.test.event.TestEventA;
import dash.test.event.TestEventB;
import io.ib67.dash.AbstractBot;
import io.ib67.dash.event.EventHandler;
import io.ib67.dash.event.EventListener;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dash.test.util.Utility.forceSleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleEventRegistryTest {
    private IEventRegistry eventRegistry;
    private IEventBus eventBus;
    private AbstractBot bot;

    @BeforeEach
    public void setup(){
        eventBus = new DashEventBus(SharedResources.mainLoop,SharedResources.asyncPool);
        var factory = new SimpleEventChannelFactory(eventBus);
        eventRegistry = new SimpleEventRegistry(factory);
        bot = new MockBot();
    }

    @Test
    public void testRegularRegistration(){
        boolean[] results = new boolean[3];
        class Listeners implements EventListener {
            @EventHandler
            public void onMessage(IEventPipeline<?> pipe, TestEventA eventA){
                results[0] = true;
                pipe.fireNext();
            }
            @EventHandler
            public void onMessage(TestEventA event){
                results[1] = true;
            }
            @EventHandler
            public void onMessage(TestEventB event){
                results[2] = true;
            }
        }
        eventRegistry.registerListeners(bot,new Listeners());
        forceSleep(1);
        eventBus.postEvent(new TestEventA(0),it->{});
        forceSleep(1);
        assertFalse(results[2],"type dispatch");
        assertTrue(results[0],"pipe+event");
        assertTrue(results[1],"event only");
    }
}