package io.ib67.dash.tag;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public sealed interface Tag permits SimpleTag {
    static Tag of(String identifier){
        return new SimpleTag(identifier);
    }
    String identifier();
}
