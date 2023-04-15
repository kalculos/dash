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

import dash.internal.util.Threads;
import io.ib67.dash.scheduler.Scheduler;
import io.ib67.dash.scheduler.future.ScheduledFuture;
import io.ib67.dash.scheduler.future.TaskFuture;
import io.ib67.kiwi.future.Result;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.ib67.kiwi.Kiwi.runAny;

public class DashScheduler implements Scheduler {
    private final ScheduledExecutorService main;
    private final ScheduledExecutorService async;

    public DashScheduler(ScheduledExecutorService main, ScheduledExecutorService async) {
        this.main = main;
        this.async = async;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskFuture submit(Task task) {
        var future = new DashTaskPromise(task);
        if (Threads.isPrimaryThread()) {
            future.fromResult((Result<Void, Exception>) runAny(task::execute));
        } else {
            main.submit(() -> future.fromResult((Result<Void, Exception>) runAny(task::execute)));
        }
        return future;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskFuture submitAsync(Task task) {
        var future = new DashTaskPromise(task);
        async.submit(() -> future.fromResult((Result<Void, Exception>) runAny(task::execute)));
        return future;
    }

    @Override
    public ScheduledFuture scheduleLater(Task task, Duration duration) {
        return new DashScheduledPromise(task, main.schedule(task::execute, duration.toMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public ScheduledFuture scheduleTimer(Task task, Duration period) {
        return new DashScheduledPromise(task, main.scheduleAtFixedRate(task::execute, 0L, period.toMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public ScheduledFuture scheduleAsyncLater(Task task, Duration duration) {
        return new DashScheduledPromise(task, async.schedule(task::execute, duration.toMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public ScheduledFuture scheduleAsyncTimer(Task task, Duration period) {
        return new DashScheduledPromise(task, async.scheduleAtFixedRate(task::execute, 0L, period.toMillis(), TimeUnit.MILLISECONDS));
    }
}
