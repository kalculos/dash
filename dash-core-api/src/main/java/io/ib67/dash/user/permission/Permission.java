package io.ib67.dash.user.permission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Permission {
    @NotNull
    String getNode();

    @Nullable
    String getDescription();

    boolean matches(Permission permission);
}
