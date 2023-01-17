package dash.test.util;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.handler.EventHandlerAdapter;
import lombok.SneakyThrows;
import net.sf.persism.Session;

import java.sql.DriverManager;
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

    @SneakyThrows
    public static Session createSession(){
        Class.forName("org.h2.Driver");
        var conn = DriverManager.getConnection("jdbc:h2:mem:test");
        return new Session(conn);
    }
}
