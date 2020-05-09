package io.github.portlek.vote.requirement;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Requirement;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public final class ChanceReq implements Requirement {

    private final Random random = new Random();

    private final int chance;

    public ChanceReq(int chance) {
        this.chance = chance;
    }

    @Override
    public boolean control(@NotNull Player player) {
        return chance == 0 || chance >= random.nextInt(100) + 1;
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + "chance", chance);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%chance%", String.valueOf(chance))
        );
    }

}
