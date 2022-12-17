package io.ib67.dash.internal;

import io.ib67.dash.Dash;
import io.ib67.kiwi.Kiwi;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class DashInstFiner {
    public static final Supplier<Dash> FINDER
            = Kiwi.byLazy(() -> ServiceLoader.load(Dash.class).findFirst()
            .orElseThrow(() -> new IllegalStateException("Cannot find any dash instance")));
}
