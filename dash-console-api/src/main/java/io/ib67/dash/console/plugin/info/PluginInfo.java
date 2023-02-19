package io.ib67.dash.console.plugin.info;

import com.github.zafarkhaja.semver.Version;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record PluginInfo(
        String name,
        String description,
        Version version,
        List<String> authors,
        List<String> dependencies,
        List<String> loadBefore,
        PluginType type
) {
    public PluginInfo {
        requireNonNull(name);
        requireNonNull(version);
        requireNonNull(type);
        authors = authors == null ? List.of() : authors;
        dependencies = dependencies == null ? List.of() : dependencies;
        loadBefore = loadBefore == null ? List.of() : loadBefore;
    }
}
