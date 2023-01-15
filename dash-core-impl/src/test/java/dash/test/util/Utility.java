package dash.test.util;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.handler.EventHandlerAdapter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Utility {
    @SneakyThrows
    public static void forceSleep(int second) {
        TimeUnit.SECONDS.sleep(second);
    }

    public static <T extends AbstractEvent> EventHandlerAdapter<T> ofAdapter(Consumer<T> handler) {
        return new EventHandlerAdapter<T>() {
            @Override
            public void handleMessage(T event) {
                handler.accept(event);
            }
        };
    }
}
