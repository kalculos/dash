/*
 *
 *   AstralFlow - The plugin enriches bukkit servers
 *   Copyright (C) 2022 The Inlined Lambdas and Contributors
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *   USA
 */

package dash.internal.scheduler;

import io.ib67.dash.scheduler.AwaitingTickable;
import io.ib67.dash.scheduler.Scheduler;
import io.ib67.dash.scheduler.TickReceipt;
import io.ib67.dash.scheduler.Tickable;
import io.ib67.dash.scheduler.exception.TickTaskException;
import io.ib67.kiwi.WeakHashSet;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class SimpleCatchingScheduler implements Scheduler {
    private final Queue<AwaitingTickable<? extends Tickable<?>>> tickables = new LinkedBlockingQueue<>(256);
    private final Set<Tickable<?>> waitingForRemoval = new WeakHashSet<>();
    private final int exceptionLimiter;

    @Override
    public void tick() {
        //AstralHelper.ensureMainThread("Scheduler tick");
        var iter = tickables.iterator();
        while (iter.hasNext()) {
            var tickable = iter.next();
            if (tickable.receipt.isDropped() || waitingForRemoval.contains(tickable.tickable)) { // deactivated.
                iter.remove();
                continue;
            }
            try {
                tickable.tick();
            } catch (Throwable exception) {
                new TickTaskException("Task " + tickable.getClass().getName() + " threw an exception", exception, tickable.tickable).printStackTrace();
                if (tickable.exceptionCounter++ > exceptionLimiter) {
                    //todo Log.warn(LogCategory.SCHEDULER, "Tickable " + tickable.getClass().getName() + "#" + System.identityHashCode(tickable) + " has thrown " + exceptionLimiter + " exceptions. It will be deactivated.");
                    tickable.receipt.drop();
                    iter.remove();
                }
            }
        }
    }

    @Override
    public <T extends Tickable<T>> TickReceipt<T> add(Tickable<T> tickable) {
        //AstralHelper.ensureMainThread("Scheduler add");
        var receipt = new TickReceipt<T>();
        tickables.offer(new AwaitingTickable<>(tickable, receipt));
        tickable.setup(receipt);
        return receipt;
    }

    @Override
    public void remove(Tickable<?> tickable) {
        //AstralHelper.ensureMainThread("Scheduler remove");
        waitingForRemoval.add(tickable);
    }
}
