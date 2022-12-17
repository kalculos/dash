package io.ib67.dash.adapter;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.contact.group.ChatGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public abstract class PlatformAdapter {
    @Getter
    protected final String name;

    public List<? extends Friend> getFriends() {
        return getAllContacts().stream().filter(it -> it instanceof Friend).map(it -> (Friend) it).toList();
    }

    public abstract Optional<? extends Contact> getContact(String platformId);

    public abstract Optional<? extends Contact> getContact(long id); // todo: integrate dash data-store to map UniversalUID -> PlatformUID

    public Optional<? extends Friend> getFriend(long id) {
        return getContact(id).filter(it -> it instanceof Friend).map(it -> (Friend) it);
    }

    public Optional<? extends Friend> getFriend(String platformId) {
        return getContact(platformId).filter(it -> it instanceof Friend).map(it -> (Friend) it);
    }

    public Optional<? extends ChatGroup> getGroup(long id) {
        return getContact(id).filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it);
    }

    public Optional<? extends ChatGroup> getGroup(String platformId) {
        return getContact(platformId).filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it);
    }

    public List<? extends ChatGroup> getChatGroups() {
        return getAllContacts().stream().filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it).toList();
    }

    public abstract List<? extends Contact> getAllContacts();

    public abstract Contact getPlatformBot();
}
