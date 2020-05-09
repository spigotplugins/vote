package io.github.portlek.vote.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.hook.PlaceholderAPIWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class Placeholders {

    private Placeholders() {
    }

    @NotNull
    public static String player(@NotNull Player player, @NotNull String text) {
        return init(
            player,
            text
                .replace("%player%", player.getName())
                .replace("%player_name%", player.getName())
                .replace("%player-name%", player.getName())
        );
    }

    @NotNull
    public static String placeholders(@NotNull String text, @NotNull List<Placeholder> placeholders) {
        final AtomicReference<String> finalText = new AtomicReference<>(
            text
        );

        placeholders.forEach(placeholder ->
            finalText.set(finalText.get().replace(placeholder.getRegex(), placeholder.getReplace()))
        );

        return finalText.get();
    }

    @NotNull
    private static String init(@NotNull Player player, @NotNull String text) {
        final AtomicReference<String> finalText = new AtomicReference<>(
            new Colored(
                text
            ).toString()
        );

        Vote.getAPI().config.getWrapped("PlaceholderAPI").ifPresent(wrapped ->
            finalText.set(((PlaceholderAPIWrapper)wrapped).apply(player, finalText.get())));

        return finalText.get();
    }

}
