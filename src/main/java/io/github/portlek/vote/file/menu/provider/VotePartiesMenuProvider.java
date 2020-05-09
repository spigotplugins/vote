package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.vote.util.FileElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class VotePartiesMenuProvider implements InventoryProvider {

    @NotNull
    private final Map<String, FileElement> elements;

    public VotePartiesMenuProvider(@NotNull Map<String, FileElement> elements) {
        this.elements = elements;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Optional.ofNullable(elements.get("empty-slots")).ifPresent(fileElement ->
            fileElement.fill(inventoryContents)
        );

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

}
