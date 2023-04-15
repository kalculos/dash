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

package io.ib67.dash.console.plugin.loader;

import com.google.common.graph.MutableNetwork;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class OrderedGraphWalker<N, E> {
    enum WalkState {
        ERROR, SUCCESS, NONE
    }

    @FunctionalInterface
    public interface ExceptionalConsumer<T> {
        void accept(T t) throws Exception;
    }

    private final MutableNetwork<N, E> network;
    private final ExceptionalConsumer<N> consumer;
    private final BiConsumer<N, ? super Exception> exceptionHandler;
    private final Predicate<E> shouldPassError;
    private final Map<N, WalkState> stateMap = new HashMap<>(network.nodes().size());
    private final Set<N> walkedNodes = new HashSet<>();

    public void walk() {
        for (N node : network.nodes()) {
            if (stateMap.containsKey(node)) return;
            traverse(node);
            walkedNodes.clear();;
        }
    }

    public void cleanState(){
        var iter = stateMap.entrySet().iterator();
        while (iter.hasNext()){
            var entry = iter.next();
            switch (entry.getValue()){
                case ERROR -> network.removeNode(entry.getKey());
            }
            iter.remove();
        }
        stateMap.entrySet().removeIf(it->it.getValue() == WalkState.ERROR);
    }

    private void traverse(N node) {
        var stateMap = this.stateMap;
        var walked = this.walkedNodes;
        if(walked.contains(node)){
            stateMap.put(node,WalkState.ERROR);
            exceptionHandler.accept(node,new IllegalStateException("Circular dependency"));
            return;
        }
        walked.add(node);
        // find predecessors
        for (N predecessor : network.predecessors(node)) {
            if (!stateMap.containsKey(predecessor)) traverse(predecessor); // process it first.
            if (stateMap.get(predecessor) == WalkState.ERROR) {
                if(shouldPassError.test(network.edgeConnectingOrNull(predecessor,node))){
                    stateMap.put(node, WalkState.ERROR); // pass the error
                    exceptionHandler.accept(node, null); // oh
                    return;
                }
                // or just continue.
            }
        }

        // it's time for itself!
        try {
            consumer.accept(node);
        } catch (Exception exception) {
            // something wrong...
            exceptionHandler.accept(node, exception);
            stateMap.put(node,WalkState.ERROR);
            return;
        }
    }
}
