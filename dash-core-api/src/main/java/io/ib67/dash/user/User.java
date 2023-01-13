package io.ib67.dash.user;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.tag.Taggable;
import io.ib67.dash.user.permission.Permissible;
import io.ib67.dash.user.permission.Permission;
import io.ib67.dash.user.permission.PermissionContext;

import java.util.List;

public interface User extends Permissible, Taggable {
    long getId();

    String getName();

    List<? extends Contact> getKnownContacts();

    PermissionContext getDefaultContext();

    @Override
    default boolean hasPermission(Permission perm) {
        return hasPermission(getDefaultContext(), perm);
    }
}
