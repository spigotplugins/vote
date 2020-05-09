package io.github.portlek.vote.handle;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.User;
import io.github.portlek.vote.Vote;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class UserBasic implements User {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final Map<String, Reward> usedRewards;

    private int maxVote;

    private int voteUntilTheNextParty;

    public UserBasic(@NotNull UUID uuid, @NotNull Map<String, Reward> usedRewards, int maxVote,
                     int voteUntilTheNextParty) {
        this.uuid = uuid;
        this.usedRewards = usedRewards;
        this.maxVote = maxVote;
        this.voteUntilTheNextParty = voteUntilTheNextParty;
    }

    @Override
    public void save() {
        final IYaml yaml = Vote.getAPI().userOptions.yaml;

        yaml.set("users." + uuid.toString() + ".used-rewards", new ListOf<>(usedRewards.keySet()));
        yaml.set("users." + uuid.toString() + ".max-vote", maxVote);
        yaml.set("users." + uuid.toString() + ".vote-until-the-next-party", voteUntilTheNextParty);
    }

    @NotNull
    @Override
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    @NotNull
    @Override
    public Map<String, Reward> getUsedRewards() {
        return usedRewards;
    }

    @NotNull
    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getMaxVote() {
        return maxVote;
    }

    @Override
    public int getVoteUntilTheNextParty() {
        return voteUntilTheNextParty;
    }

    @Override
    public void setMaxVote(int maxVote) {
        this.maxVote = maxVote;
    }

    @Override
    public void setVoteUntilTheNextParty(int voteUntilTheNextParty) {
        this.voteUntilTheNextParty = voteUntilTheNextParty;
    }

}