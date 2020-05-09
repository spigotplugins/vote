package io.github.portlek.vote.file;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.handle.VotePartyBasic;
import io.github.portlek.vote.util.Parsed;
import org.bukkit.plugin.Plugin;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class PartyOptions implements Scalar<Party> {

    @NotNull
    private final IYaml yaml;
    @NotNull
    private final Plugin plugin;

    public PartyOptions(@NotNull IYaml yaml, @NotNull Plugin plugin) {
        this.yaml = yaml;
        this.plugin = plugin;
    }

    @Override
    public Party value() {
        yaml.create();

        final boolean enable = yaml.getOrSet("enable", true);
        final int requiredForParty = yaml.getOrSet("required-for-party", 10);
        final int delayBetweenRewards = yaml.getOrSet("delay-between-rewards", 1);
        final int maxGivenRewards = yaml.getOrSet("max-given-rewards", 10);
        final List<Reward> prePartyStartActions = new ArrayList<>();
        final List<Reward> partyStartActions = new ArrayList<>();
        final List<Reward> partyFinishActions = new ArrayList<>();
        final Parsed parsed = new Parsed(yaml, plugin);

        yaml.getOrCreateSection("pre-party-start-rewards").getKeys(false).forEach(s ->
            prePartyStartActions.add(parsed.parseReward(s, "pre-party-start-rewards." + s))
        );
        yaml.getOrCreateSection("party-start-rewards").getKeys(false).forEach(s ->
            partyStartActions.add(parsed.parseReward(s, "party-start-rewards." + s))
        );
        yaml.getOrCreateSection("party-finish-rewards").getKeys(false).forEach(s ->
            partyFinishActions.add(parsed.parseReward(s, "party-finish-rewards." + s))
        );

        return new Party(
            enable,
            requiredForParty,
            delayBetweenRewards,
            maxGivenRewards,
            new VotePartyBasic(
                prePartyStartActions,
                partyStartActions,
                partyFinishActions
            )
        );
    }

}
