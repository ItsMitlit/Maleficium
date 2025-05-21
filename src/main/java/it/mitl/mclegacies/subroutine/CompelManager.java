package it.mitl.mclegacies.subroutine;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "mclegacies")
public class CompelManager {
    public static final Map<UUID, FollowEntityGoal> compelGoals = new HashMap<>();
    public static final Map<UUID, Integer> compelTimers = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Map.Entry<UUID, Integer>> iter = compelTimers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<UUID, Integer> entry = iter.next();
            UUID uuid = entry.getKey();
            int ticksLeft = entry.getValue() - 1;

            if (ticksLeft <= 0) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                for (ServerLevel level : server.getAllLevels()) {
                    Entity e = level.getEntity(uuid);
                    if (e instanceof Mob mob) {
                        FollowEntityGoal goal = compelGoals.get(uuid);
                        if (goal != null) {
                            mob.goalSelector.removeGoal(goal);
                        }
                    }
                }
                compelGoals.remove(uuid);
                iter.remove();
            } else {
                entry.setValue(ticksLeft);
            }
        }
    }
}
