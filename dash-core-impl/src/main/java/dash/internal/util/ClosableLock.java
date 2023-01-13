package dash.internal.util;

import java.util.concurrent.locks.Lock;

public class ClosableLock implements AutoCloseable{
    private final Lock lock;
    public ClosableLock(Lock lock){
        lock.lock();
        this.lock = lock;
    }
    @Override
    public void close() {
        lock.unlock();
    }
}
