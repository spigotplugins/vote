package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class TeleportAct implements Action {

    @NotNull
    private final String worldName;

    private final int x;

    private final int y;

    private final int z;

    private final int yaw;

    private final int pitch;

    public TeleportAct(@NotNull String worldName, int x, int y, int z, int yaw, int pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void apply(@NotNull Player player) {
        Optional.ofNullable(Bukkit.getWorld(worldName)).ifPresent(world ->
            player.teleport(
                new Location(world, x, y, z, yaw, pitch)
            )
        );
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        apply(player);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.TELEPORT.first + ".world", worldName);
        yaml.set(path + ActionType.TELEPORT.first + ".x", x);
        yaml.set(path + ActionType.TELEPORT.first + ".y", y);
        yaml.set(path + ActionType.TELEPORT.first + ".z", z);
        yaml.set(path + ActionType.TELEPORT.first + ".yaw", yaw);
        yaml.set(path + ActionType.TELEPORT.first + ".pitch", pitch);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
