package io.github.portlek.vote.requirement;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Requirement;
import io.github.portlek.vote.util.Placeholder;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PermissionsReq implements Requirement {

    @NotNull
    private final List<String> permissions;

    public PermissionsReq(@NotNull List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean control(@NotNull Player player) {
        return permissions.stream().allMatch(player::hasPermission);
    }

    @Override
    public void save(@NotNull String path, @NotNull IYaml yaml) {
        yaml.set(path + "permissions", permissions);
    }

    @NotNull
    @Override
    public List<Placeholder> getPlaceholders() {
        return new ListOf<>(
            new Placeholder("%permissions%", permissions.toString())
        );
    }

}
