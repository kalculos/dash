package io.ib67.dash.tag;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.AvailableSince("0.1.0")
public interface Taggable {
    boolean hasTag(Tag tag);

    /**
     * add a tag to this
     * @param tag tag
     * @return false if present
     */
    boolean addTag(Tag tag);

    boolean removeTag(Tag tag);

    Collection<? extends Tag> getTags();
}
