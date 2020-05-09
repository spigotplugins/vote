package io.github.portlek.vote;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Action extends Storable {

    void apply(@NotNull Player player);

    void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders);

    @NotNull
    Action EMPTY = new Action() {
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
