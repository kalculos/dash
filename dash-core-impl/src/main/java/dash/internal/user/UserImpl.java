package dash.internal.user;

import dash.internal.user.data.ContactData;
import io.ib67.dash.Dash;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.tag.Tag;
import io.ib67.dash.user.User;
import io.ib67.dash.user.permission.Permission;
import io.ib67.dash.user.permission.PermissionContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class UserImpl implements User {
    private final Set<Tag> tags = new HashSet<>();
    private final long id;
    private String name;
    private final List<Permission> permissions;

    private final List<ContactData> contactData;

    @Override
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    @Override
    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }

    @Override
    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    @Override
    public List<? extends Contact> getKnownContacts() {
        return contactData.stream().map(it -> Dash.getInstance().getAdapterRegistry().getAdapter(it.getPlatformId()).orElseThrow().getContact(it.getId()).orElseThrow()).toList();
    }

    @Override
    public PermissionContext getDefaultContext() {
        return PermissionContext.DEFAULT; // todo
    }

    @Override
    public boolean hasPermission(PermissionContext context, Permission permission) {
        return switch (context.lookup(this, permission)) {
            case DEFAULT -> permissions.stream().anyMatch(it -> it.matches(permission));
            case BLOCK -> false;
            case BYPASS -> true;
        };
    }

    @Override
    public void grant(Permission permission) {
        this.permissions.add(permission);
    }

    @Override
    public void revoke(Permission permission) {
        this.permissions.remove(permission);
    }
}
