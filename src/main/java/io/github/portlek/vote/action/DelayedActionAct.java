package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DelayedActionAct implements Action {

    private final int delay;

    @NotNull
    private final List<Reward> rewards;

    public DelayedActionAct(int delay, @NotNull List<Reward> rewards) {
        this.delay = delay;
        this.rewards = rewards;
    }

    @Override
    public void apply(@NotNull Player player) {
        Bukkit.getScheduler().runTaskLater(Vote.getAPI().vote, () ->
                rewards.forEach(action -> action.apply(player)),
            delay
        );
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        Bukkit.getScheduler().runTaskLater(Vote.getAPI().vote, () ->
                rewards.forEach(action -> action.applyWithPlaceholders(player, placeholders)),
            delay
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.DELAYED_ACTION.first + ".delay", delay);
        rewards.forEach(reward -> reward.save(path + ActionType.DELAYED_ACTION.first + ".rewards.", yaml));
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
