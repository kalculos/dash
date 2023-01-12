package io.ib67.dash.user;

import io.ib67.dash.user.permission.Permission;

public interface IPermissionFactory {
    Permission parseNode(String node);

    Permission registerPermission(String node,String description);
}
