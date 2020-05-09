package io.github.portlek.vote;

import com.vexsoftware.votifier.model.VotifierEvent;
import fr.minuskube.inv.SmartInventory;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.vote.file.*;
import io.github.portlek.vote.file.menu.Menus;
import io.github.portlek.vote.file.menu.MenusOptions;
import io.github.portlek.vote.util.ListenerBasic;
import io.github.portlek.vote.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cactoos.collection.Filtered;
import org.cactoos.collection.Reversed;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.list.Sorted;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.SumOf;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class VoteAPI {

    public final Map<UUID, Reward> editChanceAnvilMenu = new HashMap<>();
    public final Map<UUID, String> editChanceAnvilMenuPath = new HashMap<>();
    public final Map<UUID, IYaml> editChanceAnvilMenuYaml = new HashMap<>();
    public final Map<UUID, SmartInventory> editChanceAnvilMenuInventory = new HashMap<>();

    @NotNull
    private final ConfigOptions configOptions;

    @NotNull
    private final LanguageOptions languageOptions;

    @NotNull
    public final RewardsOptions rewardsOptions;

    @NotNull
    public final UserOptions userOptions;

    @NotNull
    private final PartyOptions partyOptions;

    @NotNull
    public final MenusOptions menusOptions;

    @NotNull
    public final Vote vote;

    @NotNull
    public Config config;

    @NotNull
    public Language language;

    @NotNull
    public Rewards rewards;

    @NotNull
    public Users users;

    @NotNull
    public Party party;

    @NotNull
    public Menus menus;

    public VoteAPI(@NotNull Vote vote) {
        this.vote = vote;
        this.configOptions = new ConfigOptions(
            new YamlOf(vote, "config")
        );
        this.config = configOptions.value();
        this.languageOptions = new LanguageOptions(
            new YamlOf(vote, "languages", config.pluginLanguage),
            config.pluginPrefix
        );
        this.language = languageOptions.value();
        rewardsOptions = new RewardsOptions(
            new YamlOf(vote, "rewards"),
            vote
        );
        this.rewards = rewardsOptions.value();
        this.userOptions = new UserOptions(
            new YamlOf(vote, "data"),
            rewards
        );
        this.users = userOptions.value();
        this.partyOptions = new PartyOptions(
            new YamlOf(vote, "party"), vote
        );
        this.party = partyOptions.value();
        this.menusOptions = new MenusOptions(
            this
        );
        this.menus = menusOptions.value();
    }

    public void reloadPlugin(boolean first) {
        if (!first) {
            vote.getServer().getScheduler().cancelTasks(vote);
            clear();

            config = configOptions.value();
            language = languageOptions.value();
            rewards = rewardsOptions.value();
            users = userOptions.value();
            party = partyOptions.value();
        } else {
            checkForUpdate(vote.getServer().getConsoleSender());
            new ListenerBasic<>(
                PlayerJoinEvent.class,
                event -> event.getPlayer().hasPermission("vote.version"),
                event -> checkForUpdate(event.getPlayer())
            ).register(vote);
            new ListenerBasic<>(
                VotifierEvent.class,
                event -> {
                    Optional.ofNullable(Bukkit.getPlayer(event.getVote().getUsername())).ifPresent(player ->
                        Vote.getAPI().users.getOrCreateUser(player.getUniqueId()).ifPresent(user -> {
                            Vote.getAPI().rewards.rewards.values().forEach(reward -> reward.apply(player));
                            user.setMaxVote(user.getMaxVote() + 1);
                            user.setVoteUntilTheNextParty(user.getVoteUntilTheNextParty() + 1);
                            user.save();
                        })
                    );
                    Vote.getAPI().checkParty(false, false);
                }
            ).register(vote);
        }

        party.voteParty.stopAll();
    }

    public void checkForUpdate(@NotNull CommandSender sender) {
        if (!config.updateChecker) {
            vote.getLogger().warning("Update checker of the plugin was disabled.");
            return;
        }

        final UpdateChecker updater = new UpdateChecker(vote, 73329);

        try {
            if (updater.checkForUpdates()) {
                sender.sendMessage(
                    language.generalNewVersionFound(updater.getLatestVersion())
                );
            } else {
                sender.sendMessage(
                    language.generalLatestVersion(updater.getLatestVersion())
                );
            }
        } catch (Exception exception) {
            vote.getLogger().warning("Update checker failed, could not connect to the API.");
        }
    }

    public void checkParty(boolean force, boolean all) {
        checkParty(force, all, new ListOf<>());
    }

    public void checkParty(boolean force, boolean all, @NotNull List<Player> players) {
        final Collection<User> users = new Mapped<>(
            object ->
                Vote.getAPI().users.getOrCreateUser(object.getUniqueId())
                    .orElseThrow(UnsupportedOperationException::new),
            Bukkit.getOnlinePlayers()
        );
        final int amount = new SumOf(
            new Mapped<>(
                User::getVoteUntilTheNextParty,
                users
            )
        ).intValue();

        if (!force && amount < Vote.getAPI().party.requiredForParty) {
            return;
        }

        final List<User> finalList = new ArrayList<>();

        if (all) {
            finalList.addAll(users);
        } else if (players.isEmpty()) {
            finalList.addAll(
                new Filtered<>(
                    user -> user.getVoteUntilTheNextParty() > 0,
                    users
                )
            );
        } else {
            finalList.addAll(
                new Mapped<>(
                    player -> this.users.getOrCreateUser(player.getUniqueId())
                        .orElseThrow(UnsupportedOperationException::new),
                    players
                )
            );
        }

        Vote.getAPI().party.voteParty.start(
            finalList
        );
        users.forEach(user -> {
            user.setVoteUntilTheNextParty(0);
            user.save();
        });
    }

    @NotNull
    public String getHighestVotePlayer(int order) {

        final Map<UUID, Integer> highest = sortByValue(
            new MapOf<UUID, Integer>(
                new Mapped<User, Map.Entry<UUID, Integer>>(
                    user -> new MapEntry<UUID, Integer>(
                        user.getUuid(),
                        user.getMaxVote()
                    ),
                    users.values()
                )
            )
        );

        if (highest.size() < order) {
            return "";
        }

        final OfflinePlayer player = vote.getServer().getOfflinePlayer(
            new ListOf<>(
                new Reversed<>(
                    sortByValue(
                        highest
                    ).entrySet()
                )
            ).get(order - 1).getKey()
        );

        if (player.getName() == null) {
            return "";
        }

        return player.getName();
    }

    public int getHighestVote(int order) {
        final List<Integer> highest = new ListOf<>(
            new Reversed<>(
                new Sorted<>(
                    new Mapped<>(
                        User::getMaxVote,
                        users.values()
                    )
                )
            )
        );

        try {
            return highest.get(order - 1);
        } catch (Exception ignored) {
            // ignored
        }

        return 0;
    }

    private void clear() {
        editChanceAnvilMenu.clear();
        editChanceAnvilMenuPath.clear();
        editChanceAnvilMenuYaml.clear();
        editChanceAnvilMenuInventory.clear();
    }

    private  <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue());

        final Map<K, V> result = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
