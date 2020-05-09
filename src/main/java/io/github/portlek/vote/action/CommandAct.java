package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static io.github.portlek.vote.util.Placeholders.placeholders;
import static io.github.portlek.vote.util.Placeholders.player;

public final class CommandAct implements Action {

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final Map<String, Map.Entry<List<String>, Boolean>> commands;

    @NotNull
    private final List<Reward> parents;

    public CommandAct(@NotNull Plugin plugin, @NotNull Map<String, Map.Entry<List<String>, Boolean>> commands,
                      @NotNull List<Reward> parents) {
        this.plugin = plugin;
        this.commands = commands;
        this.parents = parents;
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        commands.values().forEach(entry -> {
            if (entry.getValue()) {
                entry.getKey().forEach(command -> Bukkit.getScheduler().callSyncMethod(plugin, () ->
                    player.performCommand(
                        player(
                            player,
                            placeholders(
                                command,
                                placeholders
                            )
                        )
                    )
                ));
            } else {
                entry.getKey().forEach(command -> Bukkit.getScheduler().callSyncMethod(plugin, () ->
                    Bukkit.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(),
                        player(
                            player,
                            placeholders(
                                command,
                                placeholders
                            )
                        )
                    )
                ));
            }
        });
        parents.forEach(action ->
            action.apply(
                player
            )
        );
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        commands.forEach((s, entry) -> {
            yaml.set(path + ActionType.COMMAND.first + s + ".as-player", entry.getValue());
            yaml.set(path + ActionType.COMMAND.first + s + ".run", entry.getKey());
        });
        parents.forEach(reward -> reward.save(path + ".rewards.", yaml));
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
