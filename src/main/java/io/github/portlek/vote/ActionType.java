package io.github.portlek.vote;

import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ActionType {

    ITEMS("items", "item"),
    COMMAND("command", "commands", "cmd"),
    MESSAGE("message", "messages", "msg"),
    DELAYED_ACTION("delayed-action"),
    BROADCAST("broadcast"),
    JSON_BROADCAST("json-broadcast"),
    JSON_MESSAGE("json-message"),
    SOUND("sound"),
    VAULT_GIVE("vault-give"),
    VAULT_TAKE("vault-take"),
    TELEPORT("teleport", "tp"),
    TITLE_BROADCAST("tile-broadcast"),
    TITLE("title"),
    ACTIONBAR_BROADCAST("actionbar-broadcast"),
    ACTIONBAR("actionbar"),
    SEND_SERVER("send-server", "server"),
    FIREWORK("firework", "fireworks", "fire-work", "fire-works"),
    NONE("none");

    @NotNull
    private final List<String> types;

    @NotNull
    public final String first;

    ActionType(String... types) {
        this.types = new ListOf<>(types);
        this.first = this.types.get(0);
    }

    @NotNull
    public static ActionType fromString(@NotNull String name) {
        for (ActionType actionType : values()) {
            if (actionType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return actionType;
            }
        }

        return NONE;
    }

}
