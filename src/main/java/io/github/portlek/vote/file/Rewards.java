package io.github.portlek.vote.file;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class Rewards {

    @NotNull
    public final Map<String, Reward> rewards;

    @NotNull
    private final IYaml yaml;

    public Rewards(@NotNull Map<String, Reward> rewards, @NotNull IYaml yaml) {
        this.rewards = rewards;
        this.yaml = yaml;
    }

    @NotNull
    public Optional<Reward> findRewardById(@NotNull String rewardId) {
        return Optional.ofNullable(rewards.get(rewardId));
    }

    public void save(@NotNull Reward reward) {
        if (rewards.containsValue(reward)) {
            reward.save("rewards.", yaml);
        }
    }

    public void remove(@NotNull Reward reward) {
        rewards.remove(reward.getId());
        yaml.set("rewards." + reward.getId(), null);
    }

    public void create(@NotNull Reward reward) {
        if (!rewards.containsKey(reward.getId())) {
            rewards.put(reward.getId(), reward);
            save(reward);
        }
    }

}
