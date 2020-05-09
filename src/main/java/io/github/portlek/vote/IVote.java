package io.github.portlek.vote;

import org.jetbrains.annotations.NotNull;

public interface IVote {

    @NotNull
    String getServiceName();

    @NotNull
    String getUsername();

    @NotNull
    String getAddress();

    @NotNull
    String getTimeStamp();

    @NotNull
    @Override
    String toString();

}