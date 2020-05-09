package io.github.portlek.vote.util;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.*;
import io.github.portlek.vote.action.*;
import io.github.portlek.vote.handle.RewardBasic;
import io.github.portlek.vote.requirement.ChanceReq;
import io.github.portlek.vote.requirement.PermissionsReq;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.plugin.Plugin;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parsed {

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final Plugin plugin;

    public Parsed(@NotNull IYaml yaml, @NotNull Plugin plugin) {
        this.yaml = yaml;
        this.plugin = plugin;
    }

    @NotNull
    public Reward parseReward(@NotNull String rewardId, @NotNull String path) {
        return new RewardBasic(
            rewardId,
            parseRequirements(path + ".requirements"),
            parseActions(path + ".actions"),
            yaml.getOrSet(path + ".for-each", false),
            yaml.getOrSet(path + ".repetitive", false)
        );
    }

    @NotNull
    private Map<RequirementType, Requirement> parseRequirements(@NotNull String path) {
        final Map<RequirementType, Requirement> requirements = new HashMap<>();

        yaml.getOrCreateSection(path).getKeys(false).forEach(s -> {
            final Map.Entry<RequirementType, Requirement> requirementEntry = parseRequirement(path, s);

            requirements.put(requirementEntry.getKey(), requirementEntry.getValue());
        });

        return requirements;
    }

    @NotNull
    private Map<ActionType, Action> parseActions(@NotNull String path) {
        final Map<ActionType, Action> actions = new HashMap<>();

        yaml.getOrCreateSection(path).getKeys(false).forEach(s -> {
            final Map.Entry<ActionType, Action> actionEntry = parseAction(path, s);

            actions.put(actionEntry.getKey(), actionEntry.getValue());
        });

        return actions;
    }

    @NotNull
    private Map.Entry<RequirementType, Requirement> parseRequirement(@NotNull String path,
                                         @NotNull String reqType) {
        final String finalPath = path + "." + reqType;
        final RequirementType requirementType = RequirementType.fromString(reqType);

        switch (requirementType) {
            case CHANCE:
                return new MapEntry<>(RequirementType.CHANCE, new ChanceReq(yaml.getOrSet(finalPath, 0)));
            case PERMISSION:
                final Object object = yaml.get(finalPath, "");

                if (object instanceof String) {
                    return new MapEntry<>(RequirementType.PERMISSION, new PermissionsReq(
                        new ListOf<>((String) object)
                    ));
                } else if (object instanceof List) {
                    return new MapEntry<>(RequirementType.PERMISSION, new PermissionsReq(
                        (List<String>) object
                    ));
                }

                return new MapEntry<>(RequirementType.NONE, Requirement.EMPTY);
            case NONE:
            default:
                return new MapEntry<>(RequirementType.NONE, Requirement.EMPTY);
        }
    }

    @NotNull
    private Map.Entry<ActionType, Action> parseAction(@NotNull String path,
                               @NotNull String actType) {
        final String finalPath = path + "." + actType;
        final ActionType actionType = ActionType.fromString(actType);

        switch (actionType) {
            case ITEMS:
                final List<ItemAct> itemActs = new ArrayList<>();

                yaml.getOrCreateSection(finalPath).getKeys(false).forEach(s -> {
                    final List<Reward> rewards = new ArrayList<>();

                    yaml.getOrCreateSection(finalPath + "." + s + ".rewards").getKeys(false).forEach(id ->
                        rewards.add(
                            parseReward(id, finalPath + "." + s + ".rewards." + id)
                        )
                    );
                    itemActs.add(
                        new ItemAct(
                            yaml.getCustomItemStack(finalPath + "." + s),
                            rewards
                        )
                    );
                });

                return new MapEntry<>(ActionType.ITEMS, new ItemsAct(itemActs));
            case SOUND:
                return new MapEntry<>(ActionType.SOUND, new SoundAct(
                    yaml.getOrSet(finalPath, "AMBIENT_CAVE")
                ));
            case TITLE:
                return new MapEntry<>(ActionType.TITLE, new TitleAct(
                    yaml.getOrSet(finalPath + ".title", ""),
                    yaml.getOrSet(finalPath + ".sub-title", ""),
                    yaml.getOrSet(finalPath + ".fade-in", 20),
                    yaml.getOrSet(finalPath + ".show-time", 20),
                    yaml.getOrSet(finalPath + ".fade-out", 20)
                ));
            case COMMAND:
                final Map<String, Map.Entry<List<String>, Boolean>> commands = new HashMap<>();
                final List<Reward> rewards = new ArrayList<>();

                yaml.getOrCreateSection(finalPath).getKeys(false).forEach(s -> {
                    yaml.getOrCreateSection(finalPath + "." + s + ".rewards").getKeys(false).forEach(id ->
                        rewards.add(
                            parseReward(id, finalPath + "." + s + ".rewards." + id)
                        )
                    );
                    commands.put(
                        s,
                        new MapEntry<>(
                            yaml.getOrSet(finalPath + "." + s + ".run", new ListOf<>()),
                            yaml.getOrSet(finalPath + "." + s + ".as-player", false)
                        )
                    );
                });

                return new MapEntry<>(ActionType.COMMAND, new CommandAct(
                    plugin,
                    commands,
                    rewards
                ));
            case MESSAGE:
                final Object message = yaml.get(finalPath, "");

                if (message instanceof String) {
                    return new MapEntry<>(ActionType.MESSAGE, new MessageAct(
                        new ListOf<>((String) message)
                    ));
                } else if (message instanceof List) {
                    return new MapEntry<>(ActionType.MESSAGE, new MessageAct(
                        (List<String>) message
                    ));
                }

                return new MapEntry<>(ActionType.NONE, Action.EMPTY);
            case TELEPORT:
                return new MapEntry<>(ActionType.TELEPORT, new TeleportAct(
                    yaml.getOrSet(finalPath + ".world", ""),
                    yaml.getOrSet(finalPath + ".x", 0),
                    yaml.getOrSet(finalPath + ".y", 0),
                    yaml.getOrSet(finalPath + ".z", 0),
                    yaml.getOrSet(finalPath + ".yaw", -1),
                    yaml.getOrSet(finalPath + ".pitch", -1)
                ));
            case ACTIONBAR:
                return new MapEntry<>(ActionType.ACTIONBAR, new ActionbarAct(
                    yaml.getOrSet(finalPath, "")
                ));
            case BROADCAST:
                final Object broadcast = yaml.get(finalPath, "");

                if (broadcast instanceof String) {
                    return new MapEntry<>(ActionType.BROADCAST, new BroadcastAct(
                        new ListOf<>((String) broadcast)
                    ));
                } else if (broadcast instanceof List<?>) {
                    return new MapEntry<>(ActionType.BROADCAST, new BroadcastAct(
                        (List<String>) broadcast
                    ));
                }

                return new MapEntry<>(ActionType.NONE, Action.EMPTY);
            case VAULT_GIVE:
                return new MapEntry<>(ActionType.VAULT_GIVE, new VaultGiveAct(
                    yaml.getOrSet(finalPath, 0)
                ));
            case VAULT_TAKE:
                return new MapEntry<>(ActionType.VAULT_TAKE, new VaultTakeAct(
                    yaml.getOrSet(finalPath, 0)
                ));
            case SEND_SERVER:
                return new MapEntry<>(ActionType.SEND_SERVER, new SendServerAct(
                    yaml.getOrSet(finalPath, "")
                ));
            case JSON_MESSAGE:
                final Object json = yaml.get(finalPath, "");

                if (json instanceof String) {
                    return new MapEntry<>(ActionType.JSON_MESSAGE, new JsonMessageAct(
                        new ListOf<>((String) json)
                    ));
                } else if (json instanceof List) {
                    return new MapEntry<>(ActionType.JSON_MESSAGE, new JsonMessageAct(
                        (List<String>) json
                    ));
                }

                return new MapEntry<>(ActionType.NONE, Action.EMPTY);
            case DELAYED_ACTION:
                final List<Reward> delayedActionRewards = new ArrayList<>();

                yaml.getOrCreateSection(finalPath + ".rewards").getKeys(false).forEach(id ->
                    delayedActionRewards.add(
                        parseReward(id, finalPath + ".rewards." + id)
                    )
                );

                return new MapEntry<>(ActionType.DELAYED_ACTION, new DelayedActionAct(
                    yaml.getOrSet(finalPath + ".delay", 20),
                    delayedActionRewards
                ));
            case JSON_BROADCAST:
                final Object jsonBroadcast = yaml.get(finalPath, "");

                if (jsonBroadcast instanceof String) {
                    return new MapEntry<>(ActionType.JSON_BROADCAST, new JsonBroadcastAct(
                        new ListOf<>((String) jsonBroadcast)
                    ));
                } else if (jsonBroadcast instanceof List) {
                    return new MapEntry<>(ActionType.JSON_BROADCAST, new JsonBroadcastAct(
                        (List<String>) jsonBroadcast
                    ));
                }

                return new MapEntry<>(ActionType.NONE, Action.EMPTY);
            case TITLE_BROADCAST:
                return new MapEntry<>(ActionType.TITLE_BROADCAST, new TitleBroadcastAct(
                    yaml.getOrSet(finalPath + ".title", ""),
                    yaml.getOrSet(finalPath + ".sub-title", ""),
                    yaml.getOrSet(finalPath + ".fade-in", 20),
                    yaml.getOrSet(finalPath + ".show-time", 20),
                    yaml.getOrSet(finalPath + ".fade-out", 20)
                ));
            case ACTIONBAR_BROADCAST:
                return new MapEntry<>(ActionType.ACTIONBAR_BROADCAST, new ActionbarBroadcastAct(
                    yaml.getOrSet(finalPath, "")
                ));
            case FIREWORK:
                final List<Color> colors = new ArrayList<>();
                final List<Color> fades = new ArrayList<>();
                final int power = yaml.getOrSet(finalPath + ".power", 1);
                final boolean flicker = yaml.getOrSet(finalPath + ".flicker", false);
                final boolean trail = yaml.getOrSet(finalPath + ".trail", false);
                FireworkEffect.Type type = FireworkEffect.Type.BALL;

                try {
                    for (String color : yaml.getStringList(finalPath + ".colors")) {
                        colors.add(
                            Color.fromRGB(Integer.parseInt("0x" + color))
                        );
                    }
                    for (String fade : yaml.getStringList(finalPath + ".fades")) {
                        fades.add(
                            Color.fromRGB(Integer.parseInt("0x" + fade))
                        );
                    }
                    type = FireworkEffect.Type.valueOf(yaml.getOrSet(finalPath + ".type", "BALL"));
                } catch (Exception e) {
                    // ignored
                }

                return new MapEntry<>(ActionType.FIREWORK, new FireworkAct(
                    power,
                    flicker,
                    trail,
                    colors,
                    fades,
                    type
                ));
            case NONE:
            default:
                return new MapEntry<>(ActionType.NONE, Action.EMPTY);
        }
    }

}
