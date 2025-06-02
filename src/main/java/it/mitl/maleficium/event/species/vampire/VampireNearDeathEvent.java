package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireNearDeathEvent {
    @SubscribeEvent
    public static void onVampireNearDeath(LivingDamageEvent event) {
        if (MaleficiumCommonConfigs.NEAR_IMMORTAL_VAMPIRES.get() == false) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!"vampire".equals(VariableManager.getSpecies(player))) return;

        if (player.getHealth() <= 1.0F) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 2);
            event.setCanceled(true);
            return;
        }
        float healthAfter = player.getHealth() - event.getAmount();
        if (healthAfter < 1.0F) {
            if (event.getSource().is(DamageTypes.IN_FIRE) ||
                    (event.getSource().is(DamageTypes.ON_FIRE) ||
                            (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) ||
                                    (event.getSource().getDirectEntity() instanceof LivingEntity attacker &&
                                    attacker.getMainHandItem().getItem() == Items.WOODEN_SWORD))) return;
            player.setHealth(1.0F);
            event.setCanceled(true);
        }
    }
}
