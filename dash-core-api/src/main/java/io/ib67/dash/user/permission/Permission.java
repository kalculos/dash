package io.ib67.dash.user.permission;

public interface Permission {
    String getNode();
    String getDescription();
    Permission getParent();
}
