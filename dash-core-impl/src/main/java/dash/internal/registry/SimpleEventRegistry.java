package dash.internal.registry;

import io.ib67.dash.AbstractBot;
import io.ib67.dash.event.*;
import io.ib67.dash.event.handler.EventHandlerAdapter;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.dash.event.handler.IEventPipeline;
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
    @SuppressWarnings("unchecked")
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

                if (mh.type().parameterCount() != 3) { // this, pipeline, event
                    if (mh.type().parameterCount() == 2) {
                        if (!AbstractEvent.class.isAssignableFrom(mh.type().parameterType(1))) {
                            log.warn(listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " subscribes to an event that is not any subtypes of AbstractEvent.");
                            if (Boolean.getBoolean("dash.allowIllegalEventType")) {
                                log.warn("Due to dash.allowIllegalEventType is set, this will be registered normally.");
                            } else {
                                log.warn("This won't be registered.");
                                continue;
                            }
                        }
                        channel.filterForType((Class<? extends AbstractEvent>) mh.type().parameterType(1)).subscribe(handlerInfo.ignoreCancelled(), ,new DelegatedSimpleHandler<>(mh,listener));
                    } else {
                        log.warn("Cannot register " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " as a listener, please refer to documentation for solution.");
                        log.warn("This won't be registered.");
                        continue;
                    }
                } else {
                    if (!AbstractEvent.class.isAssignableFrom(mh.type().parameterType(2))) {
                        log.warn(listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " subscribes to an event that is not any subtypes of AbstractEvent.");
                        if (Boolean.getBoolean("dash.allowIllegalEventType")) {
                            log.warn("Due to dash.allowIllegalEventType is set, this will be registered normally.");
                        } else {
                            log.warn("This won't be registered.");
                            continue;
                        }
                    }
                    if (!IEventPipeline.class.isAssignableFrom(mh.type().parameterType(1))) {
                        log.warn("Cannot register " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " as a listener, please refer to documentation for solution.");
                        log.warn("This won't be registered.");
                        continue;
                    }
                    channel.filterForType((Class<? extends AbstractEvent>) mh.type().parameterType(2)).subscribe(handlerInfo.ignoreCancelled(), new DelegatedEventHandler<>(mh, listener));
                }
            }
        }
    }

    @RequiredArgsConstructor
    private final class DelegatedSimpleHandler<T extends AbstractEvent> extends EventHandlerAdapter<T> {
        private final MethodHandle mh;
        private final EventListener listener;

        @Override
        @SneakyThrows
        public void handleMessage(T event) {
            mh.invoke(listener, event);
        }
    }

    private record DelegatedEventHandler<T extends AbstractEvent>(MethodHandle mh,
                                                                  EventListener listener) implements IEventHandler<T> {
        @SneakyThrows
        @Override
        public void handleMessage(@NotNull IEventPipeline<T> pipeline, T event) {
            mh.invoke(listener, pipeline, event);
        }
    }
}
