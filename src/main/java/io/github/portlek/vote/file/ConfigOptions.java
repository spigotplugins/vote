package io.github.portlek.vote.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Hook;
import io.github.portlek.vote.Wrapped;
import io.github.portlek.vote.hook.PlaceholderAPIHook;
import io.github.portlek.vote.hook.VaultHook;
import org.bukkit.Bukkit;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public final class ConfigOptions implements Scalar<Config> {

    private static final String HOOKS = "hooks.";
    private static final String VAULT = "Vault";
    private static final String PLACEHOLDER_API = "PlaceholderAPI";

    @NotNull
    private final IYaml yaml;

    public ConfigOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Config value() {
        yaml.create();

        final String pluginPrefix = yaml.getOrSet("plugin-prefix", "&6[&eVote&6]");
        final String pluginLanguage = yaml.getOrSet("plugin-language", "en");
        final boolean updateChecker = yaml.getOrSet("update-checker", true);
        final Map<String, Wrapped> wrapped = new HashMap<>();

        initiateHooks(pluginPrefix, wrapped);

        return new Config(
            pluginPrefix,
            pluginLanguage,
            updateChecker,
            wrapped
        );
    }

    private void initiateHooks(@NotNull String pluginPrefix, @NotNull Map<String, Wrapped> wrapped) {
        final boolean hooksAutoDetect = yaml.getOrSet(HOOKS + "auto-detect", true);

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + PLACEHOLDER_API, false),
            new PlaceholderAPIHook(),
            pluginPrefix,
            wrapped,
            PLACEHOLDER_API
        );

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + VAULT, false),
            new VaultHook(), pluginPrefix, wrapped, VAULT);
    }

    private void hookLittle(boolean hooksAutoDetect, boolean hooks, @NotNull Hook hook, @NotNull String pluginPrefix,
                            @NotNull Map<String, Wrapped> wrapped, @NotNull String path,
                            @NotNull BooleanSupplier supplier) {
        if ((hooksAutoDetect || hooks) && hook.initiate() && supplier.getAsBoolean()) {
            sendHookNotify(pluginPrefix, path);
            wrapped.put(path, hook.create());
            yaml.set(HOOKS + path, true);
        } else {
            yaml.set(HOOKS + path, false);
        }
    }

    private void hookLittle(boolean hooksAutoDetect, boolean hooks, @NotNull Hook hook, @NotNull String pluginPrefix,
                            @NotNull Map<String, Wrapped> wrapped, @NotNull String path) {
        hookLittle(hooksAutoDetect, hooks, hook, pluginPrefix, wrapped, path, () -> true);
    }

    private void sendHookNotify(@NotNull String pluginPrefix, @NotNull String path) {
        Bukkit.getConsoleSender().sendMessage(
            prefix(pluginPrefix, "%prefix% &r>> &a" + path + " is hooking")
        );
    }

    @NotNull
    private String prefix(@NotNull String prefix, @NotNull String text) {
        return new Colored(
            text.replace("%prefix%", prefix)
        ).value();
    }

}
