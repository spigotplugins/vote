package io.github.portlek.vote.hook;

import io.github.portlek.vote.Wrapped;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class VaultWrapper implements Wrapped {

   @NotNull
   private final Economy economy;

   public VaultWrapper(@NotNull Economy economy) {
        this.economy = economy;
    }

   public double getMoney(@NotNull Player player) {
       return economy.getBalance(player);
   }

   public void addMoney(@NotNull Player player, double money) {
       economy.depositPlayer(player, money);
   }

   public void removeMoney(@NotNull Player player, double money) {
       if (getMoney(player) < money) {
           return;
       }

       economy.withdrawPlayer(player, money);
   }

}
