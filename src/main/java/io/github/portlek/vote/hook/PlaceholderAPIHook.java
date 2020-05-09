package io.github.portlek.vote.hook;

import io.github.portlek.vote.Hook;
import io.github.portlek.vote.Wrapped;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIHook implements Hook<PlaceholderAPIPlugin> {

    private PlaceholderAPIPlugin placeholderAPI;

    @Override
    public boolean initiate() {
        return (placeholderAPI = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null;
    }

    @NotNull
    public Wrapped create() {
        if (placeholderAPI == null) {
            throw new RuntimeException("GroupManager not initiated! Use GroupManagerHook#initiate method.");
        }

        return new PlaceholderAPIWrapper();
    }

}
