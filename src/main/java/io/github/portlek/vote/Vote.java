package io.github.portlek.vote;

import co.aikar.commands.BukkitCommandManager;
import io.github.portlek.vote.command.VoteCommand;
import io.github.portlek.vote.placeholder.VoteExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Vote extends JavaPlugin {

    /*
    fireworks/particle rewards
    choose/set party length
     */

    private static VoteAPI api;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Votifier") == null) {
            throw new IllegalStateException("You have to install Votifier to use Vote plugin!");
        }

        final BukkitCommandManager manager = new BukkitCommandManager(this);

        api = new VoteAPI(this);
        new VoteExpansion(api).register();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> api.reloadPlugin(true));
        manager.registerCommand(
            new VoteCommand(api)
        );
    }

    @NotNull
    public static VoteAPI getAPI() {
        if (api == null) {
            throw new IllegalStateException("You can't use Vote before the plugin start!");
        }

        return api;
    }

}
