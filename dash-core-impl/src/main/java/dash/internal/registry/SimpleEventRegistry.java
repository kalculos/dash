package dash.internal.registry;

import io.ib67.dash.AbstractBot;
import io.ib67.dash.event.*;
import io.ib67.dash.event.handler.HandleResult;
import io.ib67.dash.event.handler.IEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Slf4j
@RequiredArgsConstructor
public class SimpleEventRegistry implements IEventRegistry {
    private final IEventChannelFactory factory;

    @SneakyThrows
    @Override
    public void registerListeners(AbstractBot bot, EventListener listener) {
        var lookup = MethodHandles.privateLookupIn(listener.getClass(), MethodHandles.lookup());
        for (Method declaredMethod : listener.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(EventHandler.class)) {
                if (Modifier.isStatic(declaredMethod.getModifiers())) {
                    continue;
                }
                var mh = lookup.unreflect(declaredMethod);
                var handlerInfo = declaredMethod.getAnnotation(EventHandler.class);
                var channel = factory.from(handlerInfo.scheduleType(), handlerInfo.name(), handlerInfo.priority());
                var ret = mh.type().returnType();
                if (ret == boolean.class || ret == Boolean.class) {
                    if (mh.type().parameterCount() != 1 + 1) {
                        log.warn("Cannot register " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " as a listener, please refer to documentation for solution.");
                        log.warn("This won't be registered.");
                        continue;
                    }
                    if (!AbstractEvent.class.isAssignableFrom(mh.type().parameterType(0 + 1))) {
                        log.warn(listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " subscribes to an event that is not any subtypes of AbstractEvent.");
                        if (Boolean.getBoolean("dash.allowIllegalEventType")) {
                            log.warn("Due to dash.allowIllegalEventType is set, this will be registered normally.");
                        } else {
                            log.warn("This won't be registered.");
                            continue;
                        }
                    }
                    channel.subscribe(new BoolEventHandler<>(mh, listener));
                } else if (ret == HandleResult.class) {
                    if (mh.type().parameterCount() != 2 + 1) {
                        log.warn("Cannot register " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " as a listener, please refer to documentation for solution.");
                        log.warn("This won't be registered.");
                        continue;
                    }
                    if (!AbstractEvent.class.isAssignableFrom(mh.type().parameterType(0 + 1)) || !IEventChannel.class.isAssignableFrom(mh.type().parameterType(1 + 1))) {
                        log.warn(listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " subscribes to an event that is not any subtypes of AbstractEvent OR 2nd parameter is not any subtypes of IEventChannel");
                        log.warn("This won't be registered.");
                        continue;
                    }
                    channel.subscribe(new MethodEventHandler<>(mh, listener));
                } else {
                    log.warn("Unexpected return type of " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString()+", please refer to documentation.");
                }
            }
        }
    }

    private record MethodEventHandler<E extends AbstractEvent>(
            MethodHandle methodHandle,
            EventListener caller
    ) implements IEventHandler<E> {

        @Override
        @SneakyThrows
        public @NotNull HandleResult handleMessage(@NotNull E event, @NotNull IEventChannel<E> channel) {
            return (HandleResult) methodHandle.invokeExact(caller, event, channel);
        }
    }

    @RequiredArgsConstructor
    private record BoolEventHandler<E extends AbstractEvent>(
            MethodHandle methodHandle,
            EventListener caller) implements IEventHandler<E> {
        @SneakyThrows
        @Override
        public @NotNull HandleResult handleMessage(@NotNull E event, @NotNull IEventChannel<E> channel) {
            var ret = methodHandle.invokeExact(caller, event);
            return (boolean) ret ? HandleResult.CONTINUE : HandleResult.CANCELLED;
        }
    }
}
