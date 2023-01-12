package io.ib67.dash.user;

import io.ib67.kiwi.Result;

import java.util.function.Consumer;

public interface IUserManager {

    Result<User,?> findUserById(long id);
    Result<User,?> findUserByName(String name);

    User createUser(Consumer<UserBuilder> configurator);
}
