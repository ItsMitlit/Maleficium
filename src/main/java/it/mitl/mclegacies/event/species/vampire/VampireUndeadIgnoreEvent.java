package it.mitl.mclegacies.event.species.vampire;

import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireUndeadIgnoreEvent {

    private static final Map<UUID, UUID> provokedMobs = new HashMap<>();
    private static final Map<UUID, Long> lastHitTimestamps = new HashMap<>();
    private static final long PROVOKE_TIMEOUT = 15 * 1000; // 15 seconds


    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event) {
        // Check if the entity changing the target is a mob
        if (!(event.getEntity() instanceof Mob mob)) return;

        // Check if the mob is an undead entity
        if (mob.getMobType() != MobType.UNDEAD) return;

        // Check if the mob is a boss (aka, the wither)
        if (mob instanceof WitherBoss) return;

        // Check if the new target is a player
        if (!(event.getNewTarget() instanceof Player player)) return;

        // Check if the player is a vampire
        if ("vampire".equals(VariableManager.getSpecies(player))) {
            // Check if the mob was provoked (attacked)
            UUID provokedPlayer = provokedMobs.get(mob.getUUID());
            if (provokedPlayer != null && provokedPlayer.equals(player.getUUID())) return;

            // Leave the player alone
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMobHurt(LivingHurtEvent event) {
        // Check if the entity being hurt is a mob
        if (!(event.getEntity() instanceof Mob mob)) return;

        // Check if the mob is an undead entity
        if (mob.getMobType() != MobType.UNDEAD) return;

        // Check if the attacker is a player
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        // Check if the player is a vampire
        if ("vampire".equals(VariableManager.getSpecies(player))) {
            // Mark the mob as provoked
            UUID mobUUID = mob.getUUID();
            provokedMobs.put(mobUUID, player.getUUID());
            lastHitTimestamps.put(mobUUID, System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (provokedMobs.isEmpty()) return;
        if (event.getEntity() instanceof Mob mob) {
            UUID mobUUID = mob.getUUID();
            provokedMobs.remove(mobUUID);
            lastHitTimestamps.remove(mobUUID);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (provokedMobs.isEmpty()) return;

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Long>> iterator = lastHitTimestamps.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            if (currentTime - entry.getValue() > PROVOKE_TIMEOUT) {
                UUID mobUUID = entry.getKey();
                iterator.remove(); // Remove the entry from the lastHitTimestamps map
                provokedMobs.remove(mobUUID); // Remove the entry from the provokedMobs map
            }
        }
    }
}
