package io.github.portlek.vote.handle;

import io.github.portlek.vote.IVote;
import org.jetbrains.annotations.NotNull;

public class VoteBasic implements IVote {

    @NotNull
    private final String serviceName;

    @NotNull
    private final String username;

    @NotNull
    private final String address;

    @NotNull
    private final String timeStamp;

    public VoteBasic(@NotNull final String serviceName,
                     @NotNull final String username,
                     @NotNull final String address,
                     @NotNull final String timeStamp) {
        this.serviceName = serviceName;
        this.username = username;
        this.address = address;
        this.timeStamp = timeStamp;
    }

    @NotNull
    public String getServiceName() {
        return serviceName;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    @NotNull
    public String getTimeStamp() {
        return timeStamp;
    }

    @NotNull
    @Override
    public String toString() {
        return "Vote (from:" + serviceName + " username:" + username
            + " address:" + address + " timeStamp:" + timeStamp + ")";
    }

}
