package io.github.portlek.vote;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface User {

    void save();

    @NotNull
    Optional<Player> getPlayer();

    @NotNull
    UUID getUuid();

    @NotNull
    Map<String, Reward> getUsedRewards();

    int getMaxVote();

    int getVoteUntilTheNextParty();

    void setMaxVote(int maxVote);

    void setVoteUntilTheNextParty(int voteUntilTheNextParty);

}
