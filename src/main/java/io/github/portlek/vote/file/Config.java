package io.github.portlek.vote.file;

import io.github.portlek.vote.Wrapped;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class Config {

    @NotNull
    public final String pluginPrefix;

    @NotNull
    public final String pluginLanguage;

    public final boolean updateChecker;

    @NotNull
    private final Map<String, Wrapped> wrapped;


    public Config(@NotNull String pluginPrefix,
                  @NotNull String pluginLanguage,
                  boolean updateChecker,
                  @NotNull Map<String, Wrapped> wrapped) {
        this.pluginPrefix = pluginPrefix;
        this.pluginLanguage = pluginLanguage;
        this.updateChecker = updateChecker;
        this.wrapped = wrapped;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Wrapped> Optional<T> getWrapped(@NotNull String wrappedId) {
        return Optional.ofNullable(
            (T) wrapped.get(wrappedId)
        );
    }

}
