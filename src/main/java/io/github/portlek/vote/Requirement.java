package io.github.portlek.vote;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Requirement extends Storable {

    boolean control(@NotNull final Player player);

    Requirement EMPTY = new Requirement() {
        @Override
        public boolean control(@NotNull Player player) {
            return false;
        }
        @Override
        public void save(@NotNull String path, @NotNull IYaml yaml) {
        }
        @NotNull
        @Override
        public List<Placeholder> getPlaceholders() {
            return new ArrayList<>();
        }
    };

}
