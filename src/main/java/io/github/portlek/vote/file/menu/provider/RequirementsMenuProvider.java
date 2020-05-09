package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Requirement;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.util.FileElement;
import org.bukkit.entity.Player;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class RequirementsMenuProvider implements InventoryProvider {

    @NotNull
    public static Map<String, FileElement> elements = new HashMap<>();

    @NotNull
    private final Reward reward;

    @NotNull
    private final IYaml yaml;

    public RequirementsMenuProvider(@NotNull Reward reward, @NotNull IYaml yaml) {
        this.reward = reward;
        this.yaml = yaml;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final String path = contents.property("path");

        Optional.ofNullable(elements.get("chance")).ifPresent(fileElement ->
            fileElement
                .replace(true, true, new ListOf<>(
                    new Joined<>(
                        new Mapped<>(
                            Requirement::getPlaceholders,
                            reward.getRequirements().values()
                        )
                    ))
                ).insert(contents, event -> {
                    Vote.getAPI().editChanceAnvilMenu.put(player.getUniqueId(), reward);
                    Vote.getAPI().editChanceAnvilMenuPath.put(player.getUniqueId(), path);
                    Vote.getAPI().editChanceAnvilMenuYaml.put(player.getUniqueId(), yaml);
                    contents.inventory().getParent().ifPresent(smartInventory ->
                        Vote.getAPI().editChanceAnvilMenuInventory.put(player.getUniqueId(), smartInventory)
                    );
                    Vote.getAPI().menus.editChanceAnvilMenu.open((Player) event.getWhoClicked());
                })
        );
        Optional.ofNullable(elements.get("permissions")).ifPresent(fileElement ->
            fileElement
                .replace(true, true, new ListOf<>(
                    new Joined<>(
                        new Mapped<>(
                            Requirement::getPlaceholders,
                            reward.getRequirements().values()
                        )
                    ))
                ).insert(contents, event -> {

            })
        );
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}
