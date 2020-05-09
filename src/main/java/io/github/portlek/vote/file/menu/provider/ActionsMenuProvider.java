package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.vote.util.FileElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ActionsMenuProvider implements InventoryProvider {

    @NotNull
    private final Map<String, FileElement> elements;

    public ActionsMenuProvider(@NotNull Map<String, FileElement> elements) {
        this.elements = elements;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
