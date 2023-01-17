package dash.internal.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClosableLockTest {
    @Test
    public void testCloseableLock(){
        var lock = new ReentrantLock();
        assertFalse(lock.isLocked());
        try(var wrapper = new ClosableLock(lock)){
            assertTrue(lock.isLocked());
        }
        assertFalse(lock.isLocked());
    }

}