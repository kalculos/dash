/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dash.internal.scheduler;

import dash.test.SharedResources;
import io.ib67.dash.scheduler.Scheduler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashSchedulerTest {
    private static Scheduler scheduler;

    @BeforeAll
    public static void setup() {
        scheduler = new DashScheduler(SharedResources.mainLoop, SharedResources.asyncPool);
    }

    @Test
    public void testRunLater() {
        AtomicBoolean bool = new AtomicBoolean(false);
        scheduler.scheduleLater(() -> bool.set(true), Duration.ofSeconds(1));
        await().atLeast(Duration.ofSeconds(1)).atMost(Duration.ofMillis(1500)).untilTrue(bool);
    }

    @Test
    public void testRunAsyncLater() {
        AtomicBoolean bool = new AtomicBoolean(false);
        scheduler.scheduleAsyncLater(() -> bool.set(true), Duration.ofSeconds(1));
        await().atLeast(Duration.ofSeconds(1)).atMost(Duration.ofMillis(1500)).untilTrue(bool);
    }

    @SneakyThrows
    @Test
    public void testSubmit() {
        AtomicBoolean bool = new AtomicBoolean(false);
        scheduler.submit(() -> bool.set(true));
        await().atMost(200, TimeUnit.MILLISECONDS).untilTrue(bool);
        var _bool = new AtomicBoolean(false);
        scheduler.submitAsync(() -> _bool.set(true));
        await().atMost(200, TimeUnit.MILLISECONDS).untilTrue(bool);
        SharedResources.mainLoop.submit(()->{
            var atomicBool = new AtomicBoolean();
            scheduler.submit(()->atomicBool.set(true));
            assertTrue(atomicBool.get());
        }).get();
    }
}
