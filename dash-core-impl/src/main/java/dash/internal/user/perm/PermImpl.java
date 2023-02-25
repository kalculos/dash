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

package dash.internal.user.perm;

import io.ib67.dash.user.permission.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PermImpl implements Permission {
    @Getter
    private final String parent;
    @Getter
    private final boolean reversed;
    @Getter
    private final String current;
    @Getter
    private final String description;

    /**
     * This : a.b.*
     * Target: a.b.c.d
     *
     * @param permission
     * @return
     */
    @Override
    public boolean matches(Permission permission) {
        if (permission instanceof PermImpl impl) {
            if (parent.equals(impl.parent)) {
                return reversed == impl.reversed == // same direction, reverse result
                        (current == null || current.equals(impl.current)); // match node
            } else if (impl.parent.startsWith(parent)) {
                return reversed == impl.reversed == (current == null);
            }
        }
        return permission.getNode().equals(getNode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permission perm)) {
            return false;
        }
        if (perm instanceof PermImpl p) {
            return p.reversed == reversed
                    && p.parent.equals(parent)
                    && p.current != null ? p.current.equals(current) : current == null;
        }
        return perm.getNode().equals(getNode());
    }

    @Override
    public @NotNull String getNode() {
        return (reversed ? "-" : "") + parent + (current == null ? ".*" : "." + current);
    }

    @Override
    public String toString() {
        return getNode();
    }
}
