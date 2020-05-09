package io.github.portlek.vote.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.itemstack.util.ListToString;
import io.github.portlek.mcyaml.IYaml;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

public final class LanguageOptions implements Scalar<Language> {

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final String pluginPrefix;

    public LanguageOptions(@NotNull IYaml yaml, @NotNull String pluginPrefix) {
        this.yaml = yaml;
        this.pluginPrefix = pluginPrefix;
    }

    @Override
    public Language value() {
        yaml.create();

        final String errorPlayerNotFound = prefix(
            yaml.getOrSet(
                "error.player-not-found",
                "%prefix% &cPlayer not found!"
            )
        );
        final String errorThereIsReward = prefix(
            yaml.getOrSet(
                "error.there-is-reward",
                "%prefix% &cThere is already reward such like that!"
            )
        );
        final String generalReloadComplete = prefix(
            yaml.getOrSet(
                "general.reload-complete",
                "%prefix% &aReload complete, &eTook &7(%ms%ms)"
            )
        );
        final String generalLatestVersion = prefix(
            yaml.getOrSet(
                "general.latest-version",
                "%prefix% &r>> &aYou''re using the latest version (v%version%)"
            )
        );
        final String generalNewVersionFound = prefix(
            yaml.getOrSet(
                "general.new-version-found",
                "%prefix% &r>> &eNew version found (v%version%)"
            )
        );
        final String links = new ListToString(
            new Mapped<>(
                this::prefix,
                yaml.getOrSet(
                    "links",
                    new ListOf<>(
                        "&6======= %prefix% &6=======",
                        "https://minecraft-mp.com/server/230245/vote/"
                    )
                )
            )
        ).toString();
        final String commands = new ListToString(
            new Mapped<>(
                this::prefix,
                yaml.getOrSet(
                    "commands",
                    new ListOf<>(
                        "&6======= %prefix% &6=======",
                        "&7/vote &r> &eShows the vote links.",
                        "&7/vote help &r> &eShows the help message.",
                        "&7/vote link/links &r> &eShows the vote links.",
                        "&7/vote reload &r> &eReloads the plugin.",
                        "&7/vote version &r> &eDoes version check.",
                        "&7/vote rewards > &eShows a menu that contains un-given rewards."
                    )
                )
            )
        ).toString();

        return new Language(
            errorPlayerNotFound,
            errorThereIsReward,
            generalReloadComplete,
            generalLatestVersion,
            generalNewVersionFound,
            links,
            commands
        );
    }

    @NotNull
    private String prefix(@NotNull String text) {
        return new Colored(
            text.replace("%prefix%", pluginPrefix)
        ).value();
    }

}
