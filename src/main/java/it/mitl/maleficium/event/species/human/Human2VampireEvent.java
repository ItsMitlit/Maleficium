package it.mitl.maleficium.event.species.human;

import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class Human2VampireEvent {

    @SubscribeEvent
    public static void onPlayerWitchDeath(LivingDeathEvent event) {
        // Check if the entity is a player
        if (!(event.getEntity() instanceof Player player)) return;
        // Check if the death was caused by a witch
        if (!(event.getSource().getEntity() instanceof Witch)) return;

        // Check if the player is human
        if (!"human".equals(VariableManager.getSpecies(player))) return;

        // Convert human to vampire
        if (Math.random() < MaleficiumCommonConfigs.VAMPIRE_WITCH_TURN_CHANCE.get()) {

            event.setCanceled(true);
            VariableManager.setSpecies("vampire", player);
            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.WITHER_SPAWN,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            player.removeAllEffects();
        }
    }
}
