package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireDeathEvent {
    @SubscribeEvent
    public static void onVampireNearDeath(LivingDamageEvent event) {
        if (MaleficiumCommonConfigs.NEAR_IMMORTAL_VAMPIRES.get() == false) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!"vampire".equals(VariableManager.getSpecies(player))) return;

        if (player.getHealth() <= 1.0F) {
            if (meetsVampireDeathConditions(event)) return;
            if (event.getSource().is(DamageTypes.STARVE)) return;
            player.getFoodData().setFoodLevel(Math.max(0, player.getFoodData().getFoodLevel() - 2));
            event.setCanceled(true);
            return;
        }
        float healthAfter = player.getHealth() - event.getAmount();
        if (healthAfter < 1.0F) {
            if (meetsVampireDeathConditions(event)) return;
            if (event.getSource().is(DamageTypes.STARVE)) return;
            player.setHealth(1.0F);
            event.setCanceled(true);
        }
    }

    public static boolean meetsVampireDeathConditions(LivingDamageEvent event) {
        return event.getSource().is(DamageTypes.IN_FIRE) || (event.getSource().is(DamageTypes.ON_FIRE) || (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) || (event.getSource().getDirectEntity() instanceof LivingEntity attacker && attacker.getMainHandItem().getItem() == Items.WOODEN_SWORD));
    }

    @SubscribeEvent
    public static void onVampireDeath(LivingDeathEvent event) {
        if ((event.getEntity() instanceof Player player) && "vampire".equals(VariableManager.getSpecies(event.getEntity()))) {
            if ("original".equals(VariableManager.getFaction(player))) {
                net.minecraft.server.MinecraftServer server = player.getServer();
                if (server != null) {
                    server.getPlayerList().getPlayers().forEach(p ->
                            p.sendSystemMessage(Component.literal("§4An Original has been slain! So too die all of its progeny."))
                    );
                }
            }
        }

    }
}
