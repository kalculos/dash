package io.ib67.dash.event;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Available choices for which thread your handler will be run on.
 */
@ApiStatus.AvailableSince("0.1.0")
public enum ScheduleType {
    /**
     * ASYNC means your handler will be called on another thread or a something like thread-pool.
     * It will be VirtualThreads when JEP 425 is out of incubator.
     * <p>
     * WARNING: ASYNC events will not guarantee your handler priority.
     */
    ASYNC(false),
    /**
     * MAIN means your handler will be called on the EventBus MainThread and always earlier than ASYNC events.
     */
    MAIN(true),
    /**
     * MONITOR means your handler will be called on the thread which publishes the event.
     * For most situations, please use {@link #MAIN} or {@link #ASYNC} instead. This is only designed for some filters.
     */
    MONITOR(true);

    @Getter
    private final boolean ordered;

    ScheduleType(boolean ordered) {
        this.ordered = ordered;
    }
}
