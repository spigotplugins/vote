package io.github.portlek.vote.util;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class UpdateChecker {

    @NotNull
    private final Plugin plugin;

    private final int project;

    @NotNull
    private URL checkURL;

    @NotNull
    private String newVersion;

    public UpdateChecker(@NotNull Plugin plugin, int projectID) {
        this.plugin = plugin;
        this.newVersion = plugin.getDescription().getVersion();
        this.project = projectID;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public String getLatestVersion() {
        return newVersion;
    }

    @NotNull
    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }
 
    public boolean checkForUpdates() throws Exception {
        final URLConnection con = checkURL.openConnection();
        newVersion = new BufferedReader(
            new InputStreamReader(
                con.getInputStream()
            )
        ).readLine();

        return !plugin.getDescription().getVersion().equals(newVersion);
    }

}