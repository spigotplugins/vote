package io.github.portlek.vote.action;

import io.github.portlek.itemstack.util.XSound;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class SoundAct implements Action {

    @NotNull
    private final String soundString;

    public SoundAct(@NotNull String soundString) {
        this.soundString = soundString;
    }

    @Override
    public void apply(@NotNull Player player) {
        XSound.matchXSound(soundString).flatMap(xSound ->
            Optional.ofNullable(xSound.parseSound())
        ).ifPresent(sound ->
            player.playSound(player.getLocation(), sound, 1, 1)
        );
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        apply(player);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.SOUND.first, soundString);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%sound%", soundString)
        );
    }

}
