package it.mitl.maleficium.subroutine;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class FollowEntityGoal extends Goal {
    private final Mob mob;
    private final LivingEntity target;
    private final double speedModifier;
    private final float stopDistance;

    public FollowEntityGoal(Mob mob, LivingEntity target, float stopDistance) {
        this.mob = mob;
        this.target = target;
        this.stopDistance = stopDistance;
        this.speedModifier = mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    @Override
    public boolean canUse() {
        return target.isAlive() && mob.distanceToSqr(target) > stopDistance * stopDistance;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        mob.getNavigation().moveTo(target, speedModifier);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    public LivingEntity getTarget() {
        return target;
    }
}

