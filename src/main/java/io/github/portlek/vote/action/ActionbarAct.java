package io.github.portlek.vote.action;

import io.github.portlek.actionbar.base.ActionBarPlayerOf;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.portlek.vote.util.Placeholders.placeholders;
import static io.github.portlek.vote.util.Placeholders.player;

public final class ActionbarAct implements Action {

    @NotNull
    private final String actionBar;

    public ActionbarAct(@NotNull String actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        new ActionBarPlayerOf(player).sendActionBar(
            player(
                player,
                placeholders(
                    actionBar,
                    placeholders
                )
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.ACTIONBAR.first, actionBar);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%actionbar%", actionBar)
        );
    }

}
