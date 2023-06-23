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

package dash.internal.user;

import dash.internal.user.perm.PermImpl;
import io.ib67.dash.user.IPermissionRegistry;
import io.ib67.dash.user.permission.IPermission;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimplePermissionRegistry implements IPermissionRegistry {
    private final Map<String, IPermission> cache = new ConcurrentHashMap<>();

    public SimplePermissionRegistry() {
        cache.put("*", new PermImpl("", false, null, "Any"));
        cache.put("-*", new PermImpl("", true, null, "Never"));
    }

    public IPermission parseNode(String node) {
        node = node.trim();
        if (node.equals("*") || node.equals("-*")) return getNode(node);
        return parseNode0(node, null);
    }

    @Override
    public IPermission getNode(String node) {
        return cache.computeIfAbsent(node, this::parseNode);
    }

    @Override
    public IPermission registerPermission(String node, String description) {
        node = node.trim().toLowerCase();
        if (cache.containsKey(node)) {
            if (cache.get(node).getDescription() != null) {
                log.warn(node + " is already registered.");
            }
        }
        var perm = parseNode0(node, description);
        cache.put(node, perm);
        return perm;
    }

    private IPermission parseNode0(String node, String description) {
        if (node.isEmpty()) {
            throw new IllegalArgumentException("node is an empty string");
        }
        boolean reversed = false;
        String parent;
        String current = null;
        if (node.startsWith("-")) {
            reversed = true;
            node = node.substring(1);
        }
        var delimiter = node.lastIndexOf('.');
        if (delimiter == -1) {
            parent = node;
        } else {
            parent = node.substring(0, delimiter);
            if (parent.isEmpty()) {
                throw new IllegalArgumentException("parent cannot be empty");
            }
            current = node.substring(delimiter + 1);
            if ("*".equals(current) || current.isEmpty()) {
                current = null;
            }
        }
        return new PermImpl(parent, reversed, current, description);
    }
}
