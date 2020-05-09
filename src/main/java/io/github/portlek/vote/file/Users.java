package io.github.portlek.vote.file;

import io.github.portlek.vote.User;
import io.github.portlek.vote.handle.UserBasic;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Users {

    @NotNull
    private final Map<UUID, User> users;

    public Users(@NotNull Map<UUID, User> users) {
        this.users = users;
    }

    public Collection<User> values() {
        return users.values();
    }

    @NotNull
    public Optional<User> getOrCreateUser(@NotNull UUID uuid) {
        final Optional<User> userOptional = Optional.ofNullable(users.get(uuid));

        if (userOptional.isPresent()) {
            return userOptional;
        }

        final User user = new UserBasic(
            uuid,
            new HashMap<>(),
            0,
            0
        );

        users.put(uuid, user);
        user.save();

        return Optional.of(user);
    }

}
