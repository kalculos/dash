package io.ib67.dash.adapter;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.contact.group.ChatGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public abstract class PlatformAdapter {
    @Getter
    protected final String platformId;

    public List<Friend> getFriends() {
        return getAllContacts().stream().filter(it -> it instanceof Friend).map(it -> (Friend) it).toList();
    }

    public List<ChatGroup> getChatGroups() {
        return getAllContacts().stream().filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it).toList();
    }

    public abstract List<Contact> getAllContacts();

    public abstract Contact getPlatformBot();
}
