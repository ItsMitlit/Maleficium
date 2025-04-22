package it.mitl.mclegacies.event;

import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Monster monster)) return; // Monsters include undead mobs

        // Only affect undead monsters
        if (!(monster.getMobType() == MobType.UNDEAD)) return;

        // Remove existing player targeting goal
        try {
            Field availableGoalsField = GoalSelector.class.getDeclaredField("availableGoals");
            availableGoalsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Set<WrappedGoal> availableGoals = (Set<WrappedGoal>) availableGoalsField.get(monster.targetSelector);

            Iterator<WrappedGoal> iterator = availableGoals.iterator();
            while (iterator.hasNext()) {
                WrappedGoal wrappedGoal = iterator.next();
                Goal goal = wrappedGoal.getGoal();
                if (goal instanceof NearestAttackableTargetGoal<?> nearest) {
                    Field targetTypeField = NearestAttackableTargetGoal.class.getDeclaredField("targetType");
                    targetTypeField.setAccessible(true);
                    Class<?> targetType = (Class<?>) targetTypeField.get(nearest);
                    if (targetType == Player.class) {
                        iterator.remove();
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Add custom targeting goal
        monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, Player.class, 10, true, false, player -> {
            if (!(player instanceof Player)) return false;

            // Check if the player is a vampire
            if ("vampire".equals(VariableManager.getSpecies(player))) {
                return false; // Don't target if they are a vampire
            }
            return true; // Otherwise, target normally
        }));
    }
}
