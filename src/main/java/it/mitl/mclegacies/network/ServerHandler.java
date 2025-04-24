package it.mitl.mclegacies.network;

import it.mitl.mclegacies.capability.blood.BloodCapability;
import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ServerHandler {
    // This could be better stored in capabilities later
    private static final Set<String> usedVillagers = new HashSet<>();

    public static void handleDiscountRequest(ServerPlayer player, UUID villagerUUID) {
        Level world = player.level();
        Entity entity = ((ServerLevel) world).getEntity(villagerUUID);

        if (entity instanceof Villager villager) {
            if (!"vampire".equals(VariableManager.getSpecies(player))) {
                return; // Player isn't a vampire
            }

            FoodData foodData = player.getFoodData();
            if (foodData.getFoodLevel() < 4) {
                player.displayClientMessage(Component.literal("§4You are too hungry to compel this villager!"), true);
                return;
            }

            boolean discountApplied = false;

            for (MerchantOffer offer : villager.getOffers()) {
                int originalCost = offer.getCostA().getCount();
                int maxDiscount = -originalCost / 2; // Maximum discount is half the original cost
                int currentDiscount = offer.getSpecialPriceDiff();

                // Apply discount but ensure it doesn't go below the maximum allowed
                if (currentDiscount > maxDiscount) {
                    offer.setSpecialPriceDiff(currentDiscount - originalCost / 2);
                    if (offer.getSpecialPriceDiff() < maxDiscount) {
                        offer.setSpecialPriceDiff(maxDiscount);
                    }
                    discountApplied = true;
                }
            }

            // Send a message to the player if a discount was applied
            if (discountApplied) {
                foodData.setFoodLevel(foodData.getFoodLevel() - 4); // Reduce hunger by 4 points (2 blood)
                player.displayClientMessage(Component.literal("§4You have compelled the villager!"), true);
            } else {
                player.displayClientMessage(Component.literal("§4You have already compelled this villager!"), true);
            }
        }
    }

    public static void handleBloodSuckRequest(ServerPlayer player, UUID entityUUID) {
        Level world = player.level();
        Entity entity = ((ServerLevel) world).getEntity(entityUUID);

        if (!"vampire".equals(VariableManager.getSpecies(player))) return;

        BloodCapability.getBloodCapability(entity).ifPresent(blood -> {
            float currentBlood = blood.getBlood();
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 255, true, false));
            }
            if (entity instanceof Villager) {
                if (currentBlood <= 2) {
                    entity.kill();
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, true, false)); // 100 ticks = 5 seconds
                    player.displayClientMessage(Component.literal("§4You drained the villager to death!"), true);
                    return;
                }
                blood.setBlood(currentBlood - 2); // Decrease blood by 2 points
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1); // Add 1 food point because there is another addition at the bottom :D
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 1); // Add 1 saturation point
            } else {
                if (currentBlood == 1) {
                    entity.kill();
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, true, false)); // 100 ticks = 5 seconds
                    player.displayClientMessage(Component.literal("§4You drained the mob to death!"), true);
                    return;
                }
                blood.setBlood(currentBlood - 1); // Decrease blood by 1 point
            }

            float newBlood = blood.getBlood();
            if (player.getFoodData().getFoodLevel() < 20) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
            }
            int newBloodInt = (int) newBlood;
            int maxBloodInt = (int) blood.getMaxBlood();
            player.displayClientMessage(Component.literal("§4You have sucked blood from this mob! (" + newBloodInt + "/" + maxBloodInt + ")"), true);
        });
    }
}