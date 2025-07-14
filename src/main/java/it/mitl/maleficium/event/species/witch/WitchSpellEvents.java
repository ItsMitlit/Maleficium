package it.mitl.maleficium.event.species.witch;

import it.mitl.maleficium.subroutine.VariableManager;
import it.mitl.maleficium.subroutine.spell.Invisique;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class WitchSpellEvents {

    // For when the player is hurt
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if ("witch".equals(VariableManager.getSpecies(player))) {
                if (Invisique.isAffected(player)) {
                    Invisique.removeInvisique(player);
                    player.displayClientMessage(Component.literal("§cYour Illusionary Cloaking Spell has been disrupted!"), true);
                }
            }
        }
    }

    // For when the effect is removed by expiry
    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Expired event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if ("witch".equals(VariableManager.getSpecies(player))) {
                if (Invisique.isAffected(player) && event.getEffectInstance() != null && event.getEffectInstance().getEffect().equals(MobEffects.INVISIBILITY)) {
                    Invisique.removeInvisique(player);
                    player.displayClientMessage(Component.literal("§cYour Illusionary Cloaking Spell has worn off!"), true);
                }
            }
        }
    }
}
