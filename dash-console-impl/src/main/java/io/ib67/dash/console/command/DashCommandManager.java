package io.ib67.dash.console.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.user.IPermissionRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

public class DashCommandManager extends CommandManager<Contact> {
    private final IPermissionRegistry permissionRegistry;

    public DashCommandManager(Executor executor, IPermissionRegistry permissionRegistry) {
        super(AsynchronousCommandExecutionCoordinator.<Contact>builder()
                .withExecutor(executor)
                .withSynchronousParsing()
                .build(), a -> true);
        requireNonNull(this.permissionRegistry = permissionRegistry);
        
    }

    @Override
    public boolean hasPermission(@NonNull Contact sender, @NonNull String permission) {
        return sender.hasPermission(permissionRegistry.getNode(permission));
    }

    @Override
    public @NonNull CommandMeta createDefaultCommandMeta() {
        return CommandMeta.simple().build();
    }
}
