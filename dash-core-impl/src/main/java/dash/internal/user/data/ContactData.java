package dash.internal.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactData {
    private long id;
    private String platformUid;
    private String platformId;
}
