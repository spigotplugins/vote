package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ItemAct implements Action {

    @NotNull
    private final ItemStack itemStack;

    @NotNull
    private final List<Reward> parent;

    public ItemAct(@NotNull ItemStack itemStack, @NotNull List<Reward> parent) {
        this.itemStack = itemStack;
        this.parent = parent;
    }

    @Override
    public void apply(@NotNull Player player) {
        final String itemName;

        if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
            itemName = itemStack.getItemMeta().getDisplayName();
        } else {
            itemName = itemStack.getType().name();
        }

        player.getInventory().addItem(itemStack);
        parent.forEach(action ->
            action.applyWithPlaceholders(
                player,
                new ListOf<>(
                    new Placeholder("%item%", itemName),
                    new Placeholder("%item_amount%", String.valueOf(itemStack.getAmount()))
                )
            )
        );
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        apply(player);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.setCustomItemStack(path, itemStack);
        parent.forEach(reward -> reward.save(path + "rewards.", yaml));
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>();
    }

}
