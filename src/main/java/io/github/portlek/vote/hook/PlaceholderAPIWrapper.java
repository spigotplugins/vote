package io.github.portlek.vote.hook;

import io.github.portlek.vote.Wrapped;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIWrapper implements Wrapped {

    @NotNull
    public String apply(@NotNull Player player, @NotNull String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}
