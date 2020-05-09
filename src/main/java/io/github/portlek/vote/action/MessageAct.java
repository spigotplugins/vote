package io.github.portlek.vote.action;

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

public final class MessageAct implements Action {

    @NotNull
    private final List<String> messages;

    public MessageAct(@NotNull List<String> messages) {
        this.messages = messages;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        messages.forEach(message ->
            player.sendMessage(
                player(
                    player,
                    placeholders(
                        message,
                        placeholders
                    )
                )
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.MESSAGE.first, messages);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
