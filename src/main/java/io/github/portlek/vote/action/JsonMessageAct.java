package io.github.portlek.vote.action;

import io.github.portlek.mcjson.base.JsonPlayerOf;
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

public final class JsonMessageAct implements Action {

    @NotNull
    private final List<String> jsonMessages;

    public JsonMessageAct(@NotNull List<String> jsonMessages) {
        this.jsonMessages = jsonMessages;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        Bukkit.getOnlinePlayers().forEach(object ->
            jsonMessages.forEach(jsonMessage ->
                new JsonPlayerOf(
                    player
                ).sendRaw(
                    player(
                        player,
                        placeholders(
                            jsonMessage,
                            placeholders
                        )
                    )
                )
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.JSON_MESSAGE.first, jsonMessages);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
