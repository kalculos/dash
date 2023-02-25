package io.ib67.dash.user;

import io.ib67.kiwi.future.Result;

import java.util.function.Consumer;

public interface IUserManager {

    Result<? extends User,?> findUserById(long id);

    User createUser(Consumer<UserBuilder> configurator);

    void saveUser(User user);
}
