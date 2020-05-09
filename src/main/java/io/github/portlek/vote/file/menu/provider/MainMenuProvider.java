package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.util.FileElement;
import org.bukkit.entity.Player;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class MainMenuProvider implements InventoryProvider {

    @NotNull
    private final Map<String, FileElement> elements;

    public MainMenuProvider(@NotNull Map<String, FileElement> elements) {
        this.elements = elements;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Optional.ofNullable(elements.get("empty-slots")).ifPresent(fileElement ->
            fileElement.fill(inventoryContents)
        );
        Optional.ofNullable(elements.get("rewards")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                Vote.getAPI().menus.rewardsMenu.open((Player) event.getWhoClicked(),
                    new MapOf<>(
                        new MapEntry<>("path", "rewards")
                    )
                );
            })
        );
        Optional.ofNullable(elements.get("vote-parties")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                Vote.getAPI().menus.votePartiesMenu.open((Player) event.getWhoClicked());
            })
        );
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

}
