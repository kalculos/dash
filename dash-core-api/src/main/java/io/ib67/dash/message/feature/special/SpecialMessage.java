package io.ib67.dash.message.feature.special;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.IMessageSource;

public abstract class SpecialMessage<T extends IMessageSource> extends AbstractMessage<T> {
    public SpecialMessage(T source, long id) {
        super(source, id);
    }
}
