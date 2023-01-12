package io.ib67.dash.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface ICancellable {
    boolean isCancelled();

    void setCancelled(boolean flag);
}
