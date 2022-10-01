package io.ib67.dash.scheduler;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface Tickable<T extends Tickable<T>> {
    void update();

    /**
     * Called when the tickable is added to the scheduler.
     *
     * @param receipt
     */
    default void setup(TickReceipt<T> receipt) {

    }
}
