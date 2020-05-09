package io.github.portlek.vote.file;

import io.github.portlek.vote.VoteParty;
import org.jetbrains.annotations.NotNull;

public final class Party {

    public final boolean enable;

    public final int requiredForParty;

    public final int delayBetweenActions;

    public final int maxGivenActions;

    @NotNull
    public final VoteParty voteParty;

    public Party(boolean enable, int requiredForParty, int delayBetweenActions, int maxGivenActions,
                 @NotNull VoteParty voteParty) {
        this.enable = enable;
        this.requiredForParty = requiredForParty;
        this.delayBetweenActions = delayBetweenActions;
        this.maxGivenActions = maxGivenActions;
        this.voteParty = voteParty;
    }
}
