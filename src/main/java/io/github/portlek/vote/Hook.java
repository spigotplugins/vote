package io.github.portlek.vote;

import org.jetbrains.annotations.NotNull;

public interface Hook<T> {

    boolean initiate();

    @NotNull
    Wrapped create();

}
