package io.ib67.dash.message.encoder;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.Text;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
@FunctionalInterface
public interface IComponentAppendable {

    void add(IMessageComponent component) throws ComponentEncodeException;

    default IComponentAppendable append(IMessageComponent component) throws ComponentEncodeException{
        add(component);
        return this;
    }

    default IComponentAppendable append(String text) throws ComponentEncodeException{
        return append(new Text(text));
    }

    /**
     * For chained invocation like: <code>builder.register(Image.class, (a,b,c) -> b.append(a.toString()).ret(true))</code>
     *
     * @return
     */
    default boolean ret(boolean ret) {
        return ret;
    }
}
