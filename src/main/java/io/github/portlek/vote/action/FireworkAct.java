package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class FireworkAct implements Action {

    private final int power;

    private final boolean flicker;

    private final boolean trail;

    @NotNull
    private final List<Color> colors;

    @NotNull
    private final List<Color> fades;

    @NotNull
    private final FireworkEffect.Type type;

    public FireworkAct(int power, boolean flicker, boolean trail, @NotNull List<Color> colors,
                       @NotNull List<Color> fades, @NotNull FireworkEffect.Type type) {
        this.power = power;
        this.flicker = flicker;
        this.trail = trail;
        this.colors = colors;
        this.fades = fades;
        this.type = type;
    }

    @Override
    public void apply(@NotNull Player player) {
        final Firework item = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        final FireworkMeta fM = item.getFireworkMeta();

        fM.setPower(power);
        fM.addEffect(
            FireworkEffect.builder().flicker(flicker).trail(trail).withColor(colors).withFade(fades).with(type).build()
        );
        item.setFireworkMeta(fM);
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        apply(player);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.FIREWORK.first + ".power", power);
        yaml.set(path + ActionType.FIREWORK.first + ".flicker", flicker);
        yaml.set(path + ActionType.FIREWORK.first + ".trail", trail);
        yaml.set(path + ActionType.FIREWORK.first + ".type", type.name());
        yaml.set(path + ActionType.FIREWORK.first + ".colors", new Mapped<>(Color::asRGB, colors));
        yaml.set(path + ActionType.FIREWORK.first + ".fades", new Mapped<>(Color::asRGB, fades));
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%power%", String.valueOf(power)),
            new Placeholder("%flicker%", String.valueOf(flicker)),
            new Placeholder("%trail%", String.valueOf(flicker)),
            new Placeholder("%colors%", colors.toString()),
            new Placeholder("%fades%", fades.toString())
        );
    }

}
