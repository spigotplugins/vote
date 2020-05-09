package io.github.portlek.vote.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.portlek.vote.VoteAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

@CommandAlias("vote")
public final class VoteCommand extends BaseCommand {

    @NotNull
    private final VoteAPI api;

    public VoteCommand(@NotNull VoteAPI api) {
        this.api = api;
    }

    @Default
    @CommandPermission("vote.command.vote")
    public void defaultCommand(CommandSender sender) {
        sender.sendMessage(
            api.language.links
        );
    }

    @Subcommand("links|link")
    @CommandPermission("vote.command.link")
    public void linkCommand(CommandSender sender) {
        sender.sendMessage(
            api.language.links
        );
    }

    @Subcommand("reload")
    @CommandPermission("vote.command.reload")
    public void reloadCommand(CommandSender sender) {
        final long ms = System.currentTimeMillis();
        api.reloadPlugin(false);
        sender.sendMessage(
            api.language.generalReloadComplete(System.currentTimeMillis() - ms)
        );
    }

    @Subcommand("version")
    @CommandPermission("vote.command.version")
    public void versionCommand(CommandSender sender) {
        api.checkForUpdate(sender);
    }

    @Subcommand("menu")
    @CommandPermission("vote.command.menu")
    public void menuCommand(Player player) {
        api.menus.mainMenu.open(player);
    }

    @Subcommand("reset")
    @CommandPermission("vote.command.reset")
    public void resetCommand(CommandSender sender) {
        api.users.values().forEach(user -> {
            user.setMaxVote(0);
            user.save();
        });
    }

    @Subcommand("voteparty|vp")
    public final class VoteParty extends BaseCommand {

        @Subcommand("start")
        @CommandPermission("vote.command.voteparty.start")
        @CommandCompletion("all|@players")
        public void voteParty(CommandSender sender, String[] args) {
            if (args.length == 0) {
                api.checkParty(true, false);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
                api.checkParty(true, true);
            } else {
                final Player player = Bukkit.getPlayer(args[0]);

                if (player == null) {
                    sender.sendMessage(
                        api.language.errorPlayerNotFound
                    );
                    return;
                }

                api.checkParty(true, false, new ListOf<>(player));
            }
        }

    }

}
