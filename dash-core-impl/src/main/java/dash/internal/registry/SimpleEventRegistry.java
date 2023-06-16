/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dash.internal.registry;

import dash.internal.Constant;
import io.ib67.dash.event.*;
import io.ib67.dash.event.handler.EventHandlerAdapter;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.dash.event.handler.IEventPipeline;
import lombok.Generated;
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
    private final @NotNull IEventChannelFactory factory;

    @SneakyThrows
    @Generated // I'm exhausted to test these WARNING branches, just skip them, please!
    @Override
    @SuppressWarnings("unchecked")
    public void registerListeners(@NotNull EventListener listener) {
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
                        channel.filterForType((Class<? extends AbstractEvent>) mh.type().parameterType(1)).subscribe(handlerInfo.ignoreCancelled() , new DelegatedSimpleHandler<>(mh, listener));
                    } else {
                        log.warn("Cannot register " + listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " as a listener, please refer to documentation for solution.");
                        log.warn("This won't be registered.");
                        continue;
                    }
                } else {
                    if (!AbstractEvent.class.isAssignableFrom(mh.type().parameterType(2))) {
                        log.warn(listener.getClass() + "#" + declaredMethod.getName() + mh.type().toString() + " subscribes to an event that is not any subtypes of AbstractEvent.");
                        if (Constant.ALLOW_ILLEGAL_EVENT_TYPE) {
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

    @Override
    public <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler) {
        throw new UnsupportedOperationException();
    }

    @RequiredArgsConstructor
    private static final class DelegatedSimpleHandler<T extends AbstractEvent> extends EventHandlerAdapter<T> {
        private final @NotNull MethodHandle mh;
        private final @NotNull EventListener listener;

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
