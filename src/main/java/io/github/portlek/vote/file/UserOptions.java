package io.github.portlek.vote.file;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.User;
import io.github.portlek.vote.handle.UserBasic;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserOptions implements Scalar<Users> {

    @NotNull
    public final IYaml yaml;

    @NotNull
    private final Rewards rewards;

    public UserOptions(@NotNull IYaml yaml, @NotNull Rewards rewards) {
        this.yaml = yaml;
        this.rewards = rewards;
    }

    @Override
    public Users value() {
        yaml.create();

        final Map<UUID, User> userMap = new HashMap<>();

        for (String uuidString : yaml.getOrCreateSection("users").getKeys(false)) {
            final UUID uuid = UUID.fromString(uuidString);
            final int maxVote = yaml.getOrSet("users." + uuidString + ".max-vote", 0);
            final int voteUntilTheNextParty = yaml.getOrSet("users." + uuidString + ".vote-until-the-next-party", 0);
            final Map<String, Reward> usedRewards = new HashMap<>();

            yaml.getStringList("users." + uuidString + ".used-rewards").forEach(s ->
                rewards.findRewardById(s).ifPresent(reward -> usedRewards.put(s, reward))
            );

            userMap.put(
                uuid,
                new UserBasic(
                    uuid,
                    usedRewards,
                    maxVote,
                    voteUntilTheNextParty
                )
            );
        }

        return new Users(
            userMap
        );
    }
}
