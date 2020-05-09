package io.github.portlek.vote.file.menu;

import fr.minuskube.inv.SmartInventory;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.file.menu.provider.EditRewardProvider;
import io.github.portlek.vote.file.menu.provider.RequirementsMenuProvider;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;

public final class Menus {

    @NotNull
    public final SmartInventory mainMenu;

    @NotNull
    public final SmartInventory rewardsMenu;

    @NotNull
    public final AnvilGUI.Builder createRewardAnvilMenu;

    @NotNull
    public final SmartInventory votePartiesMenu;

    @NotNull
    public final SmartInventory acceptMenu;

    @NotNull
    private final SmartInventory.Builder editRewardMenuBuilder;

    @NotNull
    private final SmartInventory.Builder requirementsMenuBuilder;

    @NotNull
    public final AnvilGUI.Builder editChanceAnvilMenu;

    public Menus(@NotNull SmartInventory mainMenu,
                 @NotNull SmartInventory rewardsMenu,
                 @NotNull AnvilGUI.Builder createRewardAnvilMenu,
                 @NotNull SmartInventory votePartiesMenu,
                 @NotNull SmartInventory acceptMenu,
                 @NotNull SmartInventory.Builder editRewardMenuBuilder,
                 @NotNull SmartInventory.Builder requirementsMenuBuilder,
                 @NotNull AnvilGUI.Builder editChanceAnvilMenu) {
        this.mainMenu = mainMenu;
        this.rewardsMenu = rewardsMenu;
        this.createRewardAnvilMenu = createRewardAnvilMenu;
        this.votePartiesMenu = votePartiesMenu;
        this.acceptMenu = acceptMenu;
        this.editRewardMenuBuilder = editRewardMenuBuilder;
        this.requirementsMenuBuilder = requirementsMenuBuilder;
        this.editChanceAnvilMenu = editChanceAnvilMenu;
    }

    public void openEditRewardMenu(@NotNull Player player, @NotNull Reward reward, @NotNull String path) {
        editRewardMenuBuilder
            .title(
                editRewardMenuBuilder
                    .getTitle()
                    .replace("%reward_id%", reward.getId())
            ).provider(
                new EditRewardProvider(
                    reward,
                    Vote.getAPI().rewardsOptions.yaml
                )
            ).build()
            .open(player, new MapOf<>(
                new MapEntry<>("path", path)
            ));
    }

    public void openRequirementsMenu(@NotNull SmartInventory parent, @NotNull Player player, @NotNull Reward reward,
                                     @NotNull String path, @NotNull IYaml yaml) {
        requirementsMenuBuilder
            .parent(parent)
            .provider(
                new RequirementsMenuProvider(
                    reward,
                    yaml
                )
            ).build()
            .open(player, new MapOf<>(
                new MapEntry<>("path", path)
            ));
    }

}
