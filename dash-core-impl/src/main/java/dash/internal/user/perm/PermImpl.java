package dash.internal.user.perm;

import io.ib67.dash.user.permission.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PermImpl implements Permission {
    @Getter
    private final String parent;
    @Getter
    private final boolean reversed;
    @Getter
    private final String current;
    @Getter
    private final String description;

    /**
     * This : a.b.*
     * Target: a.b.c.d
     * @param permission
     * @return
     */
    @Override
    public boolean matches(Permission permission) {
        if(permission instanceof PermImpl impl){
            if(parent.equals(impl.parent)){
                return !reversed | (current == null || current.equals(impl.current));
            }else if(impl.parent.startsWith(parent)){
                return !reversed | current == null;
            }
        }
        return permission.getNode().equals(getNode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permission perm)) {
            return false;
        }
        if (perm instanceof PermImpl p) {
            return p.reversed & reversed
                    && p.parent.equals(parent)
                    && p.current!=null ? p.current.equals(current) : current == null;
        }
        return perm.getNode().equals(getNode());
    }

    @Override
    public @NotNull String getNode() {
        return reversed ? "-" : "" + (current == null ? parent + ".*" : parent + "." + current);
    }
}
