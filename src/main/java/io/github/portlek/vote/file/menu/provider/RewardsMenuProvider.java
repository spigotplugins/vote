package io.github.portlek.vote.file.menu.provider;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.util.FileElement;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class RewardsMenuProvider implements InventoryProvider {

    @NotNull
    private final Map<String, FileElement> elements;

    public RewardsMenuProvider(@NotNull Map<String, FileElement> elements) {
        this.elements = elements;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Optional.ofNullable(elements.get("empty-slots")).ifPresent(fileElement ->
            fileElement.fill(inventoryContents)
        );
        Optional.ofNullable(elements.get("create")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event ->
                Vote.getAPI().menus.createRewardAnvilMenu.open((Player) event.getWhoClicked())
            )
        );

        final Pagination pagination = inventoryContents.pagination();
        final List<String> rewardIds = new ArrayList<>(
            Vote.getAPI().rewards.rewards.keySet()
        );
        final ClickableItem[] items = new ClickableItem[rewardIds.size()];
        final String path = inventoryContents.property("path");

        for (int i = 0; i < items.length; i++) {
            final String rewardId = rewardIds.get(i);

            items[i] = ClickableItem.from(
                createItem(
                    rewardId
                ),
                itemClickData -> {
                    final InventoryClickEvent event = itemClickData.getEvent();

                    event.setCancelled(true);

                    final Optional<Reward> rewardOptional = Vote.getAPI().rewards.findRewardById(rewardId);

                    if (!rewardOptional.isPresent()) {
                        Vote.getAPI().menus.rewardsMenu.open((Player) event.getWhoClicked(),
                            new MapOf<>(
                                new MapEntry<>("path", path)
                            )
                        );
                        return;
                    }

                    if (event.isLeftClick()) {
                        Vote.getAPI().menus.openEditRewardMenu(
                            (Player) event.getWhoClicked(),
                            rewardOptional.get(),
                            path
                        );
                    } else if (event.isRightClick()) {
                        Vote.getAPI().menus.acceptMenu.open((Player) event.getWhoClicked(),
                            new MapOf<>(
                                new MapEntry<>(
                                    "acceptReject",
                                    new MapEntry<Consumer<InventoryClickEvent>, Consumer<InventoryClickEvent>>(
                                        accept -> {
                                            accept.setCancelled(true);
                                            Vote.getAPI().rewards.remove(rewardOptional.get());
                                            Vote.getAPI().menus.rewardsMenu.open((Player) accept.getWhoClicked(),
                                                new MapOf<>(
                                                    new MapEntry<>("path", path)
                                                )
                                            );
                                        },
                                        reject -> {
                                            reject.setCancelled(true);
                                            Vote.getAPI().menus.rewardsMenu.open((Player) reject.getWhoClicked(),
                                                new MapOf<>(
                                                    new MapEntry<>("path", path)
                                                )
                                            );
                                        }
                                    )
                                )
                            )
                        );
                    }
                }
            );
        }

        pagination.setItems(items);
        pagination.setItemsPerPage((inventoryContents.inventory().getRows() - 1) * 9);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        Optional.ofNullable(elements.get("back")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                event.setCancelled(true);
                inventoryContents.inventory().getParent().ifPresent(smartInventory ->
                    smartInventory.open((Player) event.getWhoClicked())
                );
            })
        );
        Optional.ofNullable(elements.get("previous")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                event.setCancelled(true);
                Vote.getAPI().menus.rewardsMenu.open((Player) event.getWhoClicked(), pagination.previous().getPage(),
                    new MapOf<>(
                        new MapEntry<>("path", path)
                    )
                );
            })
        );
        Optional.ofNullable(elements.get("next")).ifPresent(fileElement ->
            fileElement.insert(inventoryContents, event -> {
                event.setCancelled(true);
                Vote.getAPI().menus.rewardsMenu.open((Player) event.getWhoClicked(), pagination.next().getPage(),
                    new MapOf<>(
                        new MapEntry<>("path", path)
                    )
                );
            })
        );
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }

    @NotNull
    private ItemStack createItem(@NotNull String id) {
        return Optional.ofNullable(elements.get("pattern")).map(fileElement ->
            fileElement
                .replace(true, true, new Placeholder("%reward_id%", id)).getItemStack()
        ).orElse(new ItemStack(Material.AIR));
    }

}
