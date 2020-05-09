package io.github.portlek.vote;

import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum RequirementType {

    CHANCE("chance"),
    PERMISSION("permission", "permissions", "perm", "perms"),
    NONE("none");

    private final List<String> types;

    RequirementType(String... types) {
        this.types = new ListOf<>(types);
    }

    @NotNull
    public static RequirementType fromString(@NotNull String name) {
        for (RequirementType requirementType : values()) {
            if (requirementType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return requirementType;
            }
        }

        return NONE;
    }

}
