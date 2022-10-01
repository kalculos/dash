package dash.internal.util;

public class Threads {
    public static Thread primaryThread;

    public static boolean isPrimaryThread() {
        return Thread.currentThread() == primaryThread;
    }
}
