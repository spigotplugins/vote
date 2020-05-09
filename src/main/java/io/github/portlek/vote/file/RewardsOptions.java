package io.github.portlek.vote.file;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.util.Parsed;
import org.bukkit.plugin.Plugin;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class RewardsOptions implements Scalar<Rewards> {

    @NotNull
    public final IYaml yaml;

    @NotNull
    private final Plugin plugin;

    public RewardsOptions(@NotNull IYaml yaml, @NotNull Plugin plugin) {
        this.yaml = yaml;
        this.plugin = plugin;
    }

    @Override
    public Rewards value() {
        yaml.create();

        final Map<String, Reward> rewards = new HashMap<>();
        final Parsed parsed = new Parsed(yaml, plugin);

        yaml.getOrCreateSection("rewards").getKeys(false).forEach(s ->
            rewards.put(s, parsed.parseReward(s, "rewards." + s))
        );

        return new Rewards(
            rewards,
            yaml
        );
    }
}
