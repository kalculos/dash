package dash.internal.user.data;

import lombok.Data;
import net.sf.persism.PersistableObject;
import net.sf.persism.annotations.Column;
import net.sf.persism.annotations.Join;
import net.sf.persism.annotations.Table;

import java.util.List;

@Table("t_userdata")
@Data
public class UserData extends PersistableObject<UserData> {
    @Column(primary = true, readOnly = true)
    private long id;
    private String userName;
    private long registerTime;
    @Join(to = PermData.class, onProperties = "id", toProperties = "id")
    private List<PermData> permissions;

    @Join(to = TagData.class, onProperties = "id", toProperties = "id")
    private List<TagData> tags;

    @Join(to = ContactData.class, onProperties = "id", toProperties = "id")
    private List<ContactData> knownContacts;
}
