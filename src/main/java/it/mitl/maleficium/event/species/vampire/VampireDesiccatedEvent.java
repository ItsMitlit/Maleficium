package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireDesiccatedEvent {

    public static boolean isDesiccated(Player player) {
        return player.hasEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get());
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (isDesiccated(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player && isDesiccated(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (isDesiccated(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onItemUseEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (isDesiccated(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    // Prevents mobs from targeting desiccated players
    @SubscribeEvent
    public static void onMobTargetChange(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        if (target instanceof Player player) {
            if (player.hasEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get())) {
                event.setCanceled(true);
            }
        }
    }

    // Removes desiccated players from pre-existing target lists
    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        Entity entity = event.getEntity();
        MobEffectInstance effect = event.getEffectInstance();
        if (entity instanceof Player player && effect.getEffect() == ModEffects.VAMPIRE_DESICCATED_EFFECT.get()) {
            double radius = 32.0D;
            for (Mob mob : entity.level().getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(radius))) {
                if (mob.getTarget() == player) {
                    mob.setTarget(null);
                }
            }
        }
    }
}
