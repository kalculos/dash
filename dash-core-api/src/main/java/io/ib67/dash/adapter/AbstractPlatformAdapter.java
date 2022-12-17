package io.ib67.dash.adapter;

import io.ib67.dash.contact.AbstractContact;
import io.ib67.dash.contact.AbstractFriend;
import io.ib67.dash.contact.group.AbstractChatGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public abstract class AbstractPlatformAdapter {
    @Getter
    protected final String platformId;

    public List<AbstractFriend> getFriends() {
        return getAllContacts().stream().filter(it -> it instanceof AbstractFriend).map(it -> (AbstractFriend) it).toList();
    }

    public List<AbstractChatGroup> getChatGroups() {
        return getAllContacts().stream().filter(it -> it instanceof AbstractChatGroup).map(it -> (AbstractChatGroup) it).toList();
    }

    public abstract List<AbstractContact> getAllContacts();

    public abstract AbstractContact getPlatformBot();
}
