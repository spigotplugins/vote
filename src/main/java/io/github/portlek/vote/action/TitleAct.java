package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.title.base.TitlePlayerOf;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.portlek.vote.util.Placeholders.placeholders;
import static io.github.portlek.vote.util.Placeholders.player;

public final class TitleAct implements Action {

    @NotNull
    private final String title;

    @NotNull
    private final String subTitle;

    private final int fadeIn;

    private final int showTime;

    private final int fadeOut;

    public TitleAct(@NotNull String title, @NotNull String subTitle, int fadeIn, int showTime, int fadeOut) {
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
        new TitlePlayerOf(
            player
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
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.TITLE.first + ".title", title);
        yaml.set(path + ActionType.TITLE.first + ".sub-title", subTitle);
        yaml.set(path + ActionType.TITLE.first + ".fade-in", fadeIn);
        yaml.set(path + ActionType.TITLE.first + ".show-time", showTime);
        yaml.set(path + ActionType.TITLE.first + ".fade-out", fadeOut);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%title%", title),
            new Placeholder("%sub_title%", title),
            new Placeholder("%fade_in%", String.valueOf(fadeIn)),
            new Placeholder("%show_time%", String.valueOf(showTime)),
            new Placeholder("%fade_out%", String.valueOf(fadeOut))
        );
    }

}
