package io.github.portlek.vote.placeholder;

import io.github.portlek.vote.Vote;
import io.github.portlek.vote.VoteAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class VoteExpansion extends PlaceholderExpansion {

    @NotNull
    private final VoteAPI api;

    public VoteExpansion(@NotNull VoteAPI api) {
        this.api = api;
    }

    @Override
    public String getIdentifier() {
        return "vote";
    }

    @Override
    public String getAuthor() {
        return "portlek";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.startsWith("topvoters_player")) {
            return api.getHighestVotePlayer(
                convertInt(identifier.replace("topvoters_player", ""))
            );
        } else if (identifier.startsWith("topvoters_")) {
            return String.valueOf(
                api.getHighestVote(
                    convertInt(identifier.replace("topvoters_", ""))
                )
            );
        } else {
            return "";
        }
    }

    private int convertInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
            // ignored
        }

        return 1;
    }

}
