package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.util.FileElement;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class EditRewardProvider implements InventoryProvider {

    @NotNull
    public static Map<String, FileElement> elements = new HashMap<>();

    @NotNull
    private final Reward reward;

    @NotNull
    private final IYaml yaml;

    public EditRewardProvider(@NotNull Reward reward, @NotNull IYaml yaml) {
        this.reward = reward;
        this.yaml = yaml;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        final String path = inventoryContents.property("path");

        Optional.ofNullable(elements.get("empty-slots")).ifPresent(fileElement ->
            fileElement.fill(inventoryContents)
        );
        Optional.ofNullable(elements.get("requirements")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event ->
                Vote.getAPI().menus.openRequirementsMenu(
                    inventoryContents.inventory(),
                    player,
                    reward,
                    path,
                    yaml
                )
            )
        );
        Optional.ofNullable(elements.get("actions")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {

            })
        );
        Optional.ofNullable(elements.get("for-each")).ifPresent(fileElement ->
            fileElement
                .replace(true, true, new Placeholder("%for_each%", String.valueOf(reward.isForEach())))
                .insert(inventoryContents, event -> {
                    reward.setForEach(!reward.isForEach());
                    reward.save(path + ".", yaml);
                    init(player, inventoryContents);
                }
            )
        );
        Optional.ofNullable(elements.get("repetitive")).ifPresent(fileElement ->
            fileElement
                .replace(true, true, new Placeholder("%repetitive%", String.valueOf(reward.isRepetitive())))
                .insert(inventoryContents, event -> {
                    reward.setRepetitive(!reward.isRepetitive());
                    reward.save(path + ".", yaml);
                    init(player, inventoryContents);
                }
            )
        );
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

}
