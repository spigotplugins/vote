package io.github.portlek.vote;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.util.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Storable {

    void save(@NotNull String path, @NotNull IYaml yaml);

    @NotNull
    List<Placeholder> getPlaceholders();

}
