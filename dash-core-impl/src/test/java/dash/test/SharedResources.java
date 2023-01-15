package dash.test;

import dash.internal.util.Threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SharedResources {
    public static final ScheduledExecutorService mainLoop = Executors.newScheduledThreadPool(1);
    public static final ScheduledExecutorService asyncPool = Executors.newScheduledThreadPool(2);

    static {
        mainLoop.submit(() -> Threads.primaryThread = Thread.currentThread());
    }
}
