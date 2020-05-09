package io.github.portlek.vote;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface VoteParty {

    void start(@NotNull List<User> users);

    void stopAll();

}
