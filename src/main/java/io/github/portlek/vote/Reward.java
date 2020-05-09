package io.github.portlek.vote;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Reward extends Action {

    @NotNull
    String getId();

    @NotNull
    Map<RequirementType, Requirement> getRequirements();

    void setRequirements(@NotNull Map<RequirementType, Requirement> requirements);

    @NotNull
    Map<ActionType, Action> getActions();

    void setActions(@NotNull Map<ActionType, Action> actions);

    boolean isForEach();

    void setForEach(boolean forEach);

    boolean isRepetitive();

    void setRepetitive(boolean repetitive);

    Reward EMPTY = new Reward() {
        @NotNull
        @Override
        public String getId() {
            return "";
        }
        @NotNull
        @Override
        public Map<RequirementType, Requirement> getRequirements() {
            return new HashMap<>();
        }
        @Override
        public void setRequirements(@NotNull Map<RequirementType, Requirement> requirements) {
        }
        @NotNull
        @Override
        public Map<ActionType, Action> getActions() {
            return new HashMap<>();
        }
        @Override
        public void setActions(@NotNull Map<ActionType, Action> actions) {
        }
        @Override
        public boolean isForEach() {
            return false;
        }
        @Override
        public void setForEach(boolean forEach) {
        }
        @Override
        public boolean isRepetitive() {
            return false;
        }
        @Override
        public void setRepetitive(boolean repetitive) {
        }
        @Override
        public void apply(@NotNull Player player) {
        }
        @Override
        public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        }
        @NotNull
        @Override
        public List<Placeholder> getPlaceholders() {
            return new ArrayList<>();
        }
        @Override
        public void save(@NotNull String path, @NotNull IYaml yaml) {
        }
    };

}
