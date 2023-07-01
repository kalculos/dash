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

package io.ib67.dash.console.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import io.ib67.dash.contact.IContact;
import io.ib67.dash.scheduler.IExecutor;
import io.ib67.dash.user.IPermissionRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

public class DashCommandManager extends CommandManager<IContact> {
    private final IPermissionRegistry permissionRegistry;

    public DashCommandManager(IExecutor executor, IPermissionRegistry permissionRegistry) {
        super(AsynchronousCommandExecutionCoordinator.<IContact>builder()
                .withExecutor(it -> executor.submitAsync(it::run))
                .withSynchronousParsing()
                .build(), a -> true);
        requireNonNull(this.permissionRegistry = permissionRegistry);
    }

    @Override
    public boolean hasPermission(@NonNull IContact sender, @NonNull String permission) {
        return sender.getUser().hasPermission(permissionRegistry.getNode(permission));
    }

    @Override
    public @NonNull CommandMeta createDefaultCommandMeta() {
        return CommandMeta.simple().build();
    }
}
