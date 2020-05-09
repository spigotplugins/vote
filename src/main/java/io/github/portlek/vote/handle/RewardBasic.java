package io.github.portlek.vote.handle;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.*;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RewardBasic implements Reward {

    @NotNull
    private final String id;

    @NotNull
    private Map<RequirementType, Requirement> requirements;

    @NotNull
    private Map<ActionType, Action> actions;

    private boolean forEach;

    private boolean repetitive;

    public RewardBasic(@NotNull String id, @NotNull Map<RequirementType, Requirement> requirements,
                       @NotNull Map<ActionType, Action> actions, boolean forEach, boolean repetitive) {
        this.id = id;
        this.requirements = requirements;
        this.actions = actions;
        this.forEach = forEach;
        this.repetitive = repetitive;
    }

    /**
     * ctor.
     * <p>
     * This ctor. for empty reward
     */
    public RewardBasic(@NotNull String id) {
        this(id, new HashMap<>(), new HashMap<>(), false, false);
    }

    @Override
    public void apply(@NotNull Player player) {
        applyWithPlaceholders(player, new ListOf<>());
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        if (repetitive) {
            Vote.getAPI().users.getOrCreateUser(player.getUniqueId()).ifPresent(user -> {
                if (user.getUsedRewards().get(getId()) == null) {
                    if (requirements.values().stream().allMatch(requirement -> requirement.control(player))) {
                        actions.values().forEach(action -> action.applyWithPlaceholders(player, placeholders));
                    }
                }
            });
        } else {
            if (requirements.values().stream().allMatch(requirement -> requirement.control(player))) {
                actions.values().forEach(action -> action.applyWithPlaceholders(player, placeholders));
            }
        }
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public Map<RequirementType, Requirement> getRequirements() {
        return requirements;
    }

    @Override
    public void setRequirements(@NotNull Map<RequirementType, Requirement> requirements) {
        this.requirements = requirements;
    }

    @NotNull
    @Override
    public Map<ActionType, Action> getActions() {
        return actions;
    }

    @Override
    public void setActions(@NotNull Map<ActionType, Action> actions) {
        this.actions = actions;
    }

    @Override
    public boolean isForEach() {
        return forEach;
    }

    @Override
    public void setForEach(boolean forEach) {
        this.forEach = forEach;
    }

    public boolean isRepetitive() {
        return repetitive;
    }

    @Override
    public void setRepetitive(boolean repetitive) {
        this.repetitive = repetitive;
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + id + ".for-each", forEach);
        yaml.set(path + id + ".repetitive", repetitive);
        requirements.values().forEach(requirement -> requirement.save(path + id + ".requirements.", yaml));
        actions.values().forEach(action -> action.save(path + id + ".actions.", yaml));
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        final List<Placeholder> placeholders = new ArrayList<>();

        new Mapped<>(
            Storable::getPlaceholders,
            requirements.values()
        ).forEach(placeholders::addAll);
        new Mapped<>(
            Storable::getPlaceholders,
            actions.values()
        ).forEach(placeholders::addAll);

        return placeholders;
    }

}
