package io.ib67.dash.user.permission;

import java.util.Collection;

public interface Authority extends Permissible{
    void grant(Permission permission);

    void revoke(Permission permission);

    Collection<? extends Permission> getPermissions();
}
