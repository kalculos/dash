package io.ib67.dash;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.service.Lifecycle;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * The entrypoint of your dash application.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class AbstractBot implements Lifecycle {
    /**
     * The Bot's name
     */
    @Getter
    protected final String name;

    protected AbstractBot(String name) {
        this.name = name;
    }

    /**
     * You should override this to initialize your logics.
     */
    @Override
    public abstract void onEnable();

    public IEventChannel<? extends AbstractEvent> getChannel() {
        return Dash.getInstance().getGlobalChannel();
    }
}
