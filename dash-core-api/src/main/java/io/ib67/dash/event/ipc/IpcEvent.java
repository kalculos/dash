package io.ib67.dash.event.ipc;

import io.ib67.dash.event.AbstractEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;

/**
 * A IpcEvent is an {@link AbstractEvent} comes from any peer. (including this)
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
@Getter
public abstract class IpcEvent extends AbstractEvent {
    protected final boolean local;
    protected final Instant time;
}
