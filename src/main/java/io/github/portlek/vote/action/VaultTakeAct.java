package io.github.portlek.vote.action;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Action;
import io.github.portlek.vote.ActionType;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.hook.VaultWrapper;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class VaultTakeAct implements Action {

    private final int money;

    public VaultTakeAct(int money) {
        this.money = money;
    }

    @Override
    public void apply(@NotNull Player player) {
        Vote.getAPI().config.getWrapped("Vault").ifPresent(wrapped ->
            ((VaultWrapper)wrapped).addMoney(player, money)
        );
    }

    @Override
    public void applyWithPlaceholders(@NotNull Player player, @NotNull List<Placeholder> placeholders) {
        apply(player);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + ActionType.VAULT_TAKE.first, money);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%vault_take%", String.valueOf(money))
        );
    }

}
