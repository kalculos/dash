package io.ib67.dash.user;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.tag.Tag;
import io.ib67.dash.user.permission.Permission;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
public class UserBuilder {
    private String username;
    private List<Permission> initialPermissions = List.of();
    private List<Tag> tags = List.of();
    private Contact initialContact;
}
