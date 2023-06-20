package io.ib67.dash.console.plugin.info;

import com.github.zafarkhaja.semver.Version;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record PluginInfo(
        String name,
        String description,
        String main,
        Version version,
        List<String> authors,
        List<String> dependencies,
        List<String> loadAfters) {
    public PluginInfo {
        requireNonNull(name,"plugin vendor is required");
        requireNonNull(version,"plugin version is required");
        requireNonNull(main,"plugin main is required");
        authors = authors == null ? List.of() : authors;
        dependencies = dependencies == null ? List.of() : dependencies;
        loadAfters = loadAfters == null ? List.of() : loadAfters;
    }
}
