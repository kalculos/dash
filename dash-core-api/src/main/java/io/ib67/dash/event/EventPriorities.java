package io.ib67.dash.event;

import org.jetbrains.annotations.ApiStatus;

/**
 * Here are some constants about preferred priorities for you.
 */
@ApiStatus.AvailableSince("0.1.0")
public final class EventPriorities {
    /**
     * This is for adapters. They receive events and then send it to IM servers, which needs to receive these events as fast as they can.
     */
    public static final int EARLIEST = 1;

    /**
     * Maybe some middlewares here.
     */
    public static final int EARLIER_THAN_NORMAL = 30;

    /**
     * Your business logics.
     */
    public static final int NORMAL = 50;

    /**
     * Some post-process logics.
     */
    public static final int SLOWER_THAN_NORMAL = 70;

    /**
     * For adapters. Actually you can be slower but not recommended
     */
    public static final int SLOWEST = 100;
}
