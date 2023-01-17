package dash.internal.util;

public class Threads {
    public static Thread primaryThread;

    private Threads(){
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPrimaryThread() {
        return Thread.currentThread() == primaryThread;
    }
}
