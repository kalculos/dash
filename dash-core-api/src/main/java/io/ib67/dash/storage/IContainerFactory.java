package io.ib67.dash.storage;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface IContainerFactory {
    IPersistentContainer createContainer(String namespace, String key);
}
