package io.github.portlek.vote.util;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class FileElement {

    @NotNull
    private final String id;

    @NotNull
    private final ItemStack itemStack;

    private final int x;

    private final int y;

    public FileElement(@NotNull String id, @NotNull ItemStack itemStack, int x, int y) {
        this.id = id;
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
    }

    public FileElement(@NotNull FileElement fileElement) {
        this.id = fileElement.id;
        this.itemStack = fileElement.itemStack;
        this.x = fileElement.x;
        this.y = fileElement.y;
    }

    public void insert(@NotNull InventoryContents contents, @NotNull Consumer<InventoryClickEvent> consumer) {
        contents.set(x, y, ClickableItem.of(itemStack, consumer));
    }

    public void fill(@NotNull InventoryContents contents) {
        fill(contents, event -> {});
    }

    public void fill(@NotNull InventoryContents contents, @NotNull Consumer<InventoryClickEvent> consumer) {
        contents.fill(ClickableItem.of(itemStack, consumer));
    }

    @NotNull
    public FileElement replace(boolean displayName, boolean lore, @NotNull Placeholder... placeholders) {
        return replace(displayName, lore, new ListOf<>(placeholders));
    }

    @NotNull
    public FileElement replace(boolean displayName, boolean lore, @NotNull List<Placeholder> placeholders) {
        final ItemStack clone = itemStack.clone();
        final ItemMeta itemMeta = clone.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        if (displayName && itemMeta.hasDisplayName()) {
            for (Placeholder placeholder : placeholders) {
                itemMeta.setDisplayName(placeholder.replace(itemMeta.getDisplayName()));
            }
        }

        if (lore && itemMeta.getLore() != null && itemMeta.hasLore()) {
            final List<String> finalLore = new ArrayList<>();

            itemMeta.getLore().forEach(s -> {
                final AtomicReference<String> finalString = new AtomicReference<>(s);

                placeholders.forEach(placeholder ->
                    finalString.set(placeholder.replace(finalString.get()))
                );

                finalLore.add(finalString.get());
            });

            itemMeta.setLore(finalLore);
        }

        clone.setItemMeta(itemMeta);

        return new FileElement(id, clone, x, y);
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
