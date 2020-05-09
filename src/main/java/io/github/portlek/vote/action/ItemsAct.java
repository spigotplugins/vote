package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ItemsAct implements Action {

    @NotNull
    private final List<ItemAct> itemActs;

    public ItemsAct(@NotNull List<ItemAct> itemActs) {
        this.itemActs = itemActs;
    }

    @Override
    public void apply(@NotNull Player player) {
        itemActs.forEach(itemAct -> itemAct.apply(player));
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        itemActs.forEach(itemAct -> itemAct.applyWithPlaceholders(player, placeholders));
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        for (int i = 0; i < itemActs.size(); i++) {
            itemActs.get(i).save(path + ActionType.ITEMS.first + "." + i, yaml);
        }
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
