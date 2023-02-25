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

package dash.internal.event;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.handler.IEventPipeline;
import lombok.extern.slf4j.Slf4j;

import static dash.internal.Constant.ALLOW_ASYNC_UNSUBSCRIBE;
import static java.util.Objects.requireNonNull;

@Slf4j
public class EventPipeline<E extends AbstractEvent> implements IEventPipeline<E> {

    private boolean subscribe = true;
    private final E event;
    //private final List<Runnable> pendingRegistrations = new ArrayList<>();
    private RegisteredHandler<E> node;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public EventPipeline(E event, RegisteredHandler<E> node) {
        requireNonNull(this.event = event);
        requireNonNull(this.node = node);
    }

    @Override
    public void unsubscribe() {
        if (subscribe) {
            subscribe = false;
        } else {
            throw new IllegalStateException("Listener is already unsubscribed!");
        }
        if (!ALLOW_ASYNC_UNSUBSCRIBE && node.channel().getScheduleType() == ScheduleType.ASYNC) {
            throw new IllegalStateException("Async handlers cannot be unregistered");
        }
        // unsubscribed. (current node)
        // assertion: the 1st node (which is an empty node) never unsubscribe itself.
        node.prev.next = node.next; // remove it from them
        if (node.next != null) node.next.prev = node.prev;
    }

    @Override
    public void fireNext() {
        if (node.next != null) {
            node = node.next;
            subscribe = true;
            var handleResult = channel().apply(event);
            if (handleResult == null) {
                fireNext();
                return;
            }
            try {
                ((RegisteredHandler<AbstractEvent>) node).handler().handleMessage((IEventPipeline<AbstractEvent>) this, handleResult);
            } catch (Throwable t) {
                log.warn("Cannot invoke handler " + node.handler(), t);
                fireNext();
            }
        }
        // the end of handler list
    }

    @Override
    public IEventChannel<E> channel() {
        return node.channel();
    }
}
