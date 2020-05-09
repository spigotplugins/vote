package io.github.portlek.vote.file.menu;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.vote.Requirement;
import io.github.portlek.vote.RequirementType;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.VoteAPI;
import io.github.portlek.vote.file.menu.provider.*;
import io.github.portlek.vote.handle.RewardBasic;
import io.github.portlek.vote.requirement.ChanceReq;
import io.github.portlek.vote.util.FileElement;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class MenusOptions implements Scalar<Menus> {

    @NotNull
    private final VoteAPI api;

    @NotNull
    private final InventoryManager inventoryManager;

    public MenusOptions(@NotNull VoteAPI api) {
        this.api = api;
        this.inventoryManager = new InventoryManager(api.vote);
        inventoryManager.init();
    }

    @Override
    public Menus value() {
        final SmartInventory mainMenu = load("main-menu", MainMenuProvider::new);
        final SmartInventory rewardsMenu = load("rewards-menu", RewardsMenuProvider::new, mainMenu);
        final IYaml createRewardAnvilMenuYaml = new  YamlOf(api.vote, "menus", "create-reward-anvil-menu");

        createRewardAnvilMenuYaml.create();

        final Map<Player, String> cache = new HashMap<>();
        final AnvilGUI.Builder createRewardAnvilMenu = new AnvilGUI.Builder()
            .plugin(api.vote)
            .onClose(player -> {
                final Optional<String> rewardIdOptional = Optional.ofNullable(cache.get(player));

                if (rewardIdOptional.isPresent()) {
                    Bukkit.getScheduler().runTaskLater(api.vote, () ->
                        api.rewards.findRewardById(rewardIdOptional.get()).ifPresent(reward ->
                            api.menus.openEditRewardMenu(player, reward, "rewards")
                        ), 1L
                    );
                } else {
                    Bukkit.getScheduler().runTaskLater(api.vote, () ->
                            api.menus.rewardsMenu.open(player,
                                new MapOf<>(
                                    new MapEntry<>("path", "rewards")
                                )
                            )
                        , 1L
                    );
                }

                cache.remove(player);
            })
            .onComplete((player, s) -> {
                final Optional<Reward> rewardOptional = api.rewards.findRewardById(s);

                if (rewardOptional.isPresent()) {
                    return AnvilGUI.Response.text(api.language.errorThereIsReward);
                }

                api.rewards.create(new RewardBasic(s));
                cache.put(player, s);

                return AnvilGUI.Response.close();
            }).text(createRewardAnvilMenuYaml.getOrSet("text", "Type a reward id"));
        final SmartInventory votePartiesMenu = load("vote-parties-menu", VotePartiesMenuProvider::new, mainMenu);
        final SmartInventory acceptMenu = load("accept-menu", AcceptMenuProvider::new);
        final IYaml editRewardFile = new YamlOf(api.vote, "menus", "edit-reward-menu");

        editRewardFile.create();

        EditRewardProvider.elements = loadElements(editRewardFile);
        final SmartInventory.Builder editRewardMenuBuilder = SmartInventory.builder()
            .closeable(true)
            .id("edit-reward-menu")
            .manager(inventoryManager)
            .parent(rewardsMenu)
            .title(new Colored(editRewardFile.getOrSet("title", "Edit %reward_id% Reward")).value())
            .size(editRewardFile.getOrSet("size", 27) / 9, 9);
        final IYaml requirementsMenuFile = new YamlOf(api.vote, "menus", "requirements-menu");

        requirementsMenuFile.create();

        RequirementsMenuProvider.elements = loadElements(requirementsMenuFile);
        final SmartInventory.Builder requirementsMenuBuilder = SmartInventory.builder()
            .closeable(true)
            .id("requirements-menu")
            .manager(inventoryManager)
            .title(new Colored(requirementsMenuFile.getOrSet("title", "Requirements")).value())
            .size(requirementsMenuFile.getOrSet("size", 27) / 9, 9);
        final AnvilGUI.Builder editChanceAnvilMenu = new AnvilGUI.Builder()
            .plugin(api.vote)
            .onClose(player -> {
                final Optional<Reward> rewardOptional = Optional.ofNullable(api.editChanceAnvilMenu.get(player.getUniqueId()));
                final Optional<IYaml> yamlOptional = Optional.ofNullable(api.editChanceAnvilMenuYaml.get(player.getUniqueId()));
                final Optional<String> pathOptional = Optional.ofNullable(api.editChanceAnvilMenuPath.get(player.getUniqueId()));
                final Optional<SmartInventory> inventoryOptional = Optional.ofNullable(api.editChanceAnvilMenuInventory.get(player.getUniqueId()));

                if (rewardOptional.isPresent() && pathOptional.isPresent() && yamlOptional.isPresent() &&
                    inventoryOptional.isPresent()) {
                    try {
                        Bukkit.getScheduler().runTaskLater(api.vote, () ->
                            api.rewards.findRewardById(rewardOptional.get().getId()).ifPresent(reward ->
                                api.menus.openRequirementsMenu(inventoryOptional.get(), player, reward,
                                    pathOptional.get(), yamlOptional.get())
                            ), 1L
                        );
                    } catch (Exception exception) {
                        // ignored
                    }
                } else {
                    Bukkit.getScheduler().runTaskLater(api.vote, () ->
                            api.menus.rewardsMenu.open(player,
                                new MapOf<>(
                                    new MapEntry<>("path", "rewards")
                                )
                            )
                        , 1L
                    );
                }

                api.editChanceAnvilMenu.remove(player.getUniqueId());
                api.editChanceAnvilMenuPath.remove(player.getUniqueId());
                api.editChanceAnvilMenuYaml.remove(player.getUniqueId());
                api.editChanceAnvilMenuInventory.remove(player.getUniqueId());
            })
            .onComplete((player, s) -> {
                try {
                    final Optional<Reward> rewardOptional = Optional.ofNullable(api.editChanceAnvilMenu.get(player.getUniqueId()));
                    final Optional<String> pathOptional = Optional.ofNullable(api.editChanceAnvilMenuPath.get(player.getUniqueId()));
                    final Optional<IYaml> yamlOptional = Optional.ofNullable(api.editChanceAnvilMenuYaml.get(player.getUniqueId()));
                    final int chance = Integer.parseInt(s);

                    if (rewardOptional.isPresent() && yamlOptional.isPresent() && pathOptional.isPresent() &&
                        chance >= 0 && chance <= 100) {
                        final Map<RequirementType, Requirement> requirements = rewardOptional.get().getRequirements();

                        requirements.put(RequirementType.CHANCE, new ChanceReq(chance));
                        rewardOptional.get().setRequirements(requirements);
                        rewardOptional.get().save(pathOptional.get(), yamlOptional.get());
                    }
                } catch (Exception exception) {
                    // ignored
                }

                return AnvilGUI.Response.close();
            }).text(createRewardAnvilMenuYaml.getOrSet("text", "Type a reward id"));

        return new Menus(
            mainMenu,
            rewardsMenu,
            createRewardAnvilMenu,
            votePartiesMenu,
            acceptMenu,
            editRewardMenuBuilder,
            requirementsMenuBuilder,
            editChanceAnvilMenu
        );
    }

    private SmartInventory load(@NotNull String menuName,
                                @NotNull Function<Map<String, FileElement>, InventoryProvider> provider) {
        return load(menuName, provider, null);
    }

    private SmartInventory load(@NotNull String menuName,
                                @NotNull Function<Map<String, FileElement>, InventoryProvider> provider,
                                @Nullable SmartInventory parent) {
        final IYaml yaml = new YamlOf(api.vote, "menus", menuName);

        yaml.create();

        final String title = new Colored(yaml.getOrSet("title", "Menu of " + menuName + " file")).value();
        final int size = yaml.getOrSet("size", 27);

        final SmartInventory.Builder smartInventory = SmartInventory.builder()
            .id(menuName)
            .title(title)
            .size(size / 9, 9)
            .type(InventoryType.CHEST)
            .provider(provider.apply(loadElements(yaml)))
            .manager(inventoryManager)
            .closeable(true);

        if (parent != null) {
            smartInventory.parent(parent);
        }

        return smartInventory.build();
    }

    @NotNull
    public Map<String, FileElement> loadElements(@NotNull IYaml yaml) {
        final Map<String, FileElement> elements = new HashMap<>();

        for (String elementId : yaml.getOrCreateSection("elements").getKeys(false)) {
            elements.put(elementId, new FileElement(
                elementId,
                yaml.getCustomItemStack("elements." + elementId),
                yaml.getOrSet("elements." + elementId + ".row", 0),
                yaml.getOrSet("elements." + elementId + ".column", 0)
            ));
        }
        return elements;
    }

}
