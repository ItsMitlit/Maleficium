package it.mitl.maleficium.subroutine;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class FollowEntityGoal extends Goal {
    private final Mob mob;
    private final Player player;
    private final double speed;
    private final float stopDistance;

    public FollowEntityGoal(Mob mob, Player player, double speed, float stopDistance) {
        this.mob = mob;
        this.player = player;
        this.speed = speed;
        this.stopDistance = stopDistance;
        // log to console the mob and the speed value
        System.out.println("FollowEntityGoal created for mob: " + mob.getName().getString() + " with speed: " + speed);
    }

    @Override
    public boolean canUse() {
        return mob.isAlive() && player.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return mob.isAlive() && player.isAlive();
    }

    @Override
    public void tick() {

        long followUntil = mob.getPersistentData().getLong("FollowUntil");
        if (mob.level().getGameTime() > followUntil && mob.isAlive() && player.isAlive()) {
            this.stop(); // stop navigation
            mob.getPersistentData().remove("FollowUntil"); // remove the FollowUntil data
            mob.goalSelector.removeGoal(this); // remove the goal
            return;
        }

        mob.getNavigation().moveTo(player, speed);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
        System.out.println("FollowEntityGoal stopped for mob: " + mob.getName().getString());
    }

}

