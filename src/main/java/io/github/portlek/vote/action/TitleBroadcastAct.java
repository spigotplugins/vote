package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.title.base.TitlePlayerOf;
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

public final class TitleBroadcastAct implements Action {

    @NotNull
    private final String title;

    @NotNull
    private final String subTitle;

    private final int fadeIn;

    private final int showTime;

    private final int fadeOut;

    public TitleBroadcastAct(@NotNull String title, @NotNull String subTitle, int fadeIn, int showTime, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.showTime = showTime;
        this.fadeOut = fadeOut;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        Bukkit.getOnlinePlayers().forEach(object ->
            new TitlePlayerOf(
                object
            ).sendTitle(
                player(
                    player,
                    placeholders(
                        title,
                        placeholders
                    )
                ),
                player(
                    player,
                    placeholders(
                        subTitle,
                        placeholders
                    )
                ),
                fadeIn, showTime, fadeOut
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.TITLE_BROADCAST.first + ".title", title);
        yaml.set(path + ActionType.TITLE_BROADCAST.first + ".sub-title", subTitle);
        yaml.set(path + ActionType.TITLE_BROADCAST.first + ".fade-in", fadeIn);
        yaml.set(path + ActionType.TITLE_BROADCAST.first + ".show-time", showTime);
        yaml.set(path + ActionType.TITLE_BROADCAST.first + ".fade-out", fadeOut);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%title_broadcast%", title),
            new Placeholder("%sub_title_broadcast%", title),
            new Placeholder("%fade_in_broadcast%", String.valueOf(fadeIn)),
            new Placeholder("%show_time_broadcast%", String.valueOf(showTime)),
            new Placeholder("%fade_out_broadcast%", String.valueOf(fadeOut))
        );
    }

}
