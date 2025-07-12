package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.effect.ModEffects;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;

import static it.mitl.maleficium.Maleficium.MOD_ID;
import static it.mitl.maleficium.subroutine.PlayerUtils.shouldBeDesiccated;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireDeathEvent {
    @SubscribeEvent
    public static void onVampireNearDeath(LivingDamageEvent event) {
        if (MaleficiumCommonConfigs.NEAR_IMMORTAL_VAMPIRES.get() == false) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!"vampire".equals(VariableManager.getSpecies(player))) return;

        ServerPlayer serverPlayer = (ServerPlayer) player;
        float healthAfter = player.getHealth() - event.getAmount();

        if (healthAfter < 1.0F) {
            if (meetsVampireDeathConditions(event)) return;
            if (event.getSource().is(DamageTypes.STARVE)) return;

            player.setHealth(1.0F); // Always set health to 1

            if (serverPlayer.getEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get()) != null) {
                MobEffectInstance desiccationEffect = serverPlayer.getEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get());
                int currentDuration = desiccationEffect.getDuration();
                if (currentDuration > (15 * 20)) {
                    int newDuration = Math.max(15 * 20, currentDuration - (5 * 20));
                    serverPlayer.getFoodData().setFoodLevel(1);
                    event.getEntity().removeEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get());
                    serverPlayer.getFoodData().setFoodLevel(0);
                    event.getEntity().addEffect(new MobEffectInstance(ModEffects.VAMPIRE_DESICCATION_EFFECT.get(), newDuration, desiccationEffect.getAmplifier(), false, false, true));
                }
                event.setCanceled(true);
                return;
            }

            int currentFood = player.getFoodData().getFoodLevel();
            if (currentFood > 0) {
                int newFood = currentFood - 2;
                if (newFood <= 0) {
                    player.getFoodData().setFoodLevel(0);
                    applyDesiccationEffect(serverPlayer, 1200);
                } else {
                    player.getFoodData().setFoodLevel(newFood);
                }
                event.setCanceled(true);
                return;
            }

            player.getFoodData().setFoodLevel(0);
            applyDesiccationEffect(serverPlayer, 1200);
            event.setCanceled(true);
        }
    }

    public static boolean meetsVampireDeathConditions(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        String msgId = source.getMsgId();
        event.getEntity().sendSystemMessage(Component.literal("DamageSource msgId: " + msgId));
        event.getEntity().sendSystemMessage(Component.literal("Damage Amount: " + event.getAmount()));

        return event.getSource().is(DamageTypes.IN_FIRE)
                || event.getSource().is(DamageTypes.ON_FIRE)
                || event.getSource().is(DamageTypes.LAVA)
                || event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)
                || (event.getSource().getDirectEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().getItem() == Items.WOODEN_SWORD)
                || msgId.equals("gave_up");
    }

    @SubscribeEvent
    public static void onVampireDeath(LivingDeathEvent event) {
        if ((event.getEntity() instanceof Player player) && "vampire".equals(VariableManager.getSpecies(event.getEntity()))) {
            if ("original".equals(VariableManager.getFaction(player))) {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    server.getPlayerList().getPlayers().forEach(p ->
                            p.sendSystemMessage(Component.literal("§4An Original has been slain! So too die all of its progeny."))
                    );
                }
            }
            VariableManager.setExtraHunger(0, player);
        }

    }

    private static void applyDesiccationEffect(ServerPlayer player, int duration) {
        MobEffectInstance currentEffect = player.getEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get());
        MobEffectInstance desiccatedEffect = player.getEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get());
        if (currentEffect == null && desiccatedEffect == null) {
            MobEffectInstance giveEffect = new MobEffectInstance(ModEffects.VAMPIRE_DESICCATION_EFFECT.get(), duration, 0, false, false, true);
            giveEffect.setCurativeItems(Collections.emptyList());
            player.addEffect(giveEffect);
        }
    }

//    @SubscribeEvent
//    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
//        Player player = event.getEntity();
//        if ("vampire".equals(VariableManager.getSpecies(player)) && shouldBeDesiccated(player)) {
//            MobEffectInstance effect = new MobEffectInstance(ModEffects.VAMPIRE_DESICCATED_EFFECT.get(), Integer.MAX_VALUE, 0, false, false, false);
//            player.addEffect(effect);
//        }
//    }
}
