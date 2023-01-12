package io.ib67.dash.user.permission;

import org.jetbrains.annotations.ApiStatus;

/**
 * Suggested references for Permissible
 */
@ApiStatus.AvailableSince("0.1.0")
public interface PermissionContext {
    PermissionContext DEFAULT = (a, b) -> Result.DEFAULT;

    Result lookup(Permissible permissible, Permission perm);

    enum Result {
        DEFAULT, BYPASS, BLOCK
    }
}
