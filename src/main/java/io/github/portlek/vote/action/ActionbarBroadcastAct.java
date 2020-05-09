package io.github.portlek.vote.action;

import io.github.portlek.actionbar.base.ActionBarPlayerOf;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.portlek.vote.util.Placeholders.placeholders;
import static io.github.portlek.vote.util.Placeholders.player;

public final class ActionbarBroadcastAct implements Action {

    @NotNull
    private final String actionBar;

    public ActionbarBroadcastAct(@NotNull String actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        Bukkit.getOnlinePlayers().forEach(object ->
            new ActionBarPlayerOf(
                object
            ).sendActionBar(
                player(
                    player,
                    placeholders(
                        actionBar,
                        placeholders
                    )
                )
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.ACTIONBAR_BROADCAST.first, actionBar);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%actionbar_broadcast%", actionBar)
        );
    }

}
