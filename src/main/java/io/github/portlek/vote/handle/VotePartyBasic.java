package io.github.portlek.vote.handle;

import com.google.common.collect.Queues;
import io.github.portlek.vote.Reward;
import io.github.portlek.vote.User;
import io.github.portlek.vote.Vote;
import io.github.portlek.vote.VoteParty;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class VotePartyBasic implements VoteParty {

    private static final List<BukkitTask> TASKS = new ArrayList<>();

    @NotNull
    private final Collection<Reward> preRewards;

    @NotNull
    private final Collection<Reward> startRewards;

    @NotNull
    private final Collection<Reward> finishRewards;

    public VotePartyBasic(@NotNull Collection<Reward> preRewards, @NotNull Collection<Reward> startRewards,
                          @NotNull Collection<Reward> finishRewards) {
        this.preRewards = preRewards;
        this.startRewards = startRewards;
        this.finishRewards = finishRewards;
    }

    @Override
    public void start(@NotNull List<User> users) {
        run(users, preRewards, () ->
            run(users, startRewards, () ->
                run(users, finishRewards)
            )
        );
    }

    @Override
    public void stopAll() {
        TASKS.forEach(BukkitTask::cancel);
        TASKS.clear();
    }

    private void run(@NotNull List<User> users, @NotNull Collection<Reward> rewards) {
        run(users, rewards, () -> {});
    }

    private void run(@NotNull List<User> users, @NotNull Collection<Reward> rewards, @NotNull Runnable runnable) {
        final Queue<Reward> rewardQueue = Queues.newConcurrentLinkedQueue(rewards);
        final Reward reward = rewardQueue.peek();

        if (reward == null) {
            runnable.run();
            return;
        }

        Collections.shuffle(users);

        if (reward.isForEach()) {
            run(users, reward, () -> {
                final Reward nextReward = rewardQueue.poll();

                if (nextReward == null) {
                    runnable.run();
                    return;
                }

                run(users, rewardQueue, runnable);
            });
        } else {
            TASKS.add(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        final Reward reward = rewardQueue.poll();

                        if (reward == null) {
                            cancel();
                            runnable.run();
                            return;
                        }

                        users.forEach(user ->
                            user.getPlayer().ifPresent(reward::apply)
                        );
                    }
                }.runTaskTimerAsynchronously(Vote.getAPI().vote, 0L, Vote.getAPI().party.delayBetweenActions * 20L)
            );
        }
    }

    private void run(@NotNull Collection<User> users, @NotNull Reward reward, @NotNull Runnable runnable) {
        final Queue<User> userQueue = Queues.newConcurrentLinkedQueue(users);

        TASKS.add(
            new BukkitRunnable() {
                @Override
                public void run() {
                    final User user = userQueue.poll();

                    if (user == null) {
                        cancel();
                        runnable.run();
                        return;
                    }
                    
                    user.getPlayer().ifPresent(reward::apply);
                }
            }.runTaskTimerAsynchronously(Vote.getAPI().vote, 0L, Vote.getAPI().party.delayBetweenActions * 20L)
        );
    }

}
