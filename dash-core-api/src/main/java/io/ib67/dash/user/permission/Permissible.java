package io.ib67.dash.user.permission;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

/**
 * A {@link Permissible} is an object whose ability is up to a {@link Permission}.<br>
 * Additionally, result from {@link PermissionContext} is prior to Permissible.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface Permissible {
    boolean hasPermission(PermissionContext context, Permission permission);

    void grant(Permission permission);

    void revoke(Permission permission);

    Collection<? extends Permission> getPermissions();

    default boolean hasPermission(Permission perm) {
        return hasPermission(PermissionContext.DEFAULT, perm);
    }

}
