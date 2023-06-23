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
