package io.ib67.dash.user;

import io.ib67.dash.user.permission.Permission;

public interface IPermissionRegistry {
    Permission parseNode(String node);

    /**
     * A cached version of {@link #parseNode(String)}
     * @param node perm
     * @return maybe cached perm
     */
    Permission getNode(String node);

    Permission registerPermission(String node,String description);
}
