package io.github.portlek.vote.file;

import org.jetbrains.annotations.NotNull;

public final class Language {

    @NotNull
    public final String errorPlayerNotFound;

    @NotNull
    public final String errorThereIsReward;

    @NotNull
    private final String generalReloadComplete;

    @NotNull
    private final String generalLatestVersion;

    @NotNull
    private final String generalNewVersionFound;

    @NotNull
    public final String links;

    @NotNull
    public final String commands;

    public Language(@NotNull String errorPlayerNotFound,
                    @NotNull String errorThereIsReward,
                    @NotNull String generalReloadComplete,
                    @NotNull String generalLatestVersion,
                    @NotNull String generalNewVersionFound,
                    @NotNull String links,
                    @NotNull String commands) {
        this.errorPlayerNotFound = errorPlayerNotFound;
        this.errorThereIsReward = errorThereIsReward;
        this.generalReloadComplete = generalReloadComplete;
        this.generalLatestVersion = generalLatestVersion;
        this.generalNewVersionFound = generalNewVersionFound;
        this.links = links;
        this.commands = commands;
    }

    @NotNull
    public String generalReloadComplete(long ms) {
        return ms(generalReloadComplete, ms);
    }

    @NotNull
    public String generalNewVersionFound(@NotNull String version) {
        return version(generalNewVersionFound, version);
    }

    @NotNull
    public String generalLatestVersion(@NotNull String version) {
        return version(generalLatestVersion, version);
    }

    @NotNull
    private String ms(@NotNull String text, long ms) {
        return text.replace("%ms%", String.valueOf(ms));
    }

    @NotNull
    private String version(@NotNull String text, @NotNull String version) {
        return text.replace("%version%", version);
    }

}
