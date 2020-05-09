package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.vote.util.FileElement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class AcceptMenuProvider implements InventoryProvider {

    @NotNull
    private final Map<String, FileElement> elements;

    public AcceptMenuProvider(@NotNull Map<String, FileElement> elements) {
        this.elements = elements;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Optional.ofNullable(elements.get("empty-slots")).ifPresent(fileElement ->
            fileElement.fill(inventoryContents)
        );
        final Map.Entry<Consumer<InventoryClickEvent>, Consumer<InventoryClickEvent>> acceptReject =
            inventoryContents.property("acceptReject");

        Optional.ofNullable(elements.get("accept")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                acceptReject.getKey().accept(event);
            })
        );
        Optional.ofNullable(elements.get("reject")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                acceptReject.getValue().accept(event);
            })
        );
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

}
