package it.mitl.maleficium.network;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.capability.blood.BloodCapability;
import it.mitl.maleficium.subroutine.CompelManager;
import it.mitl.maleficium.subroutine.FollowEntityGoal;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

    public static void handleCompelRequest(ServerPlayer player, UUID entityUUID) {
        ServerLevel level = player.serverLevel();
        Entity entity = level.getEntity(entityUUID);

        FoodData foodData = player.getFoodData();
        if (foodData.getFoodLevel() < 6) {
            player.displayClientMessage(Component.literal("§4You are too hungry to compel this mob!"), true);
            return;
        }

        if (entity instanceof Mob mob) {
            long gameTime = level.getGameTime();
            long followUntil = mob.level().getGameTime() + (20 * 15); // 15 seconds
            if (mob.getPersistentData().contains("FollowUntil")) {
                long existingFollowUntil = mob.getPersistentData().getLong("FollowUntil");
                if (existingFollowUntil > gameTime) {
                    player.displayClientMessage(Component.literal("§4This mob is already compelled to follow you!"), true);
                    return;
                }
            }
            mob.getPersistentData().putLong("FollowUntil", followUntil);
            FollowEntityGoal followGoal = new FollowEntityGoal(mob, player, 1.2D, 2.0F);
            mob.goalSelector.addGoal(1, followGoal);
            player.displayClientMessage(Component.literal("§4You have compelled this mob to follow you! (15 sec)"), true);
//            if (!CompelManager.compelGoals.containsKey(entityUUID)) {
//
//
//                CompelManager.compelGoals.put(entityUUID, followGoal);
//                CompelManager.compelTimers.put(entityUUID, 300); // 15 seconds
//
//            }
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
                    Advancement aFulfillingMealAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "a_fulfilling_meal"));
                    Advancement enemyOfTheVillageAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "enemy_of_the_village"));
                    player.getAdvancements().award(aFulfillingMealAdvancement, "a_fulfilling_meal");
                    player.getAdvancements().award(enemyOfTheVillageAdvancement, "enemy_of_the_village");
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
                    Advancement aFulfillingMealAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "a_fulfilling_meal"));
                    player.getAdvancements().award(aFulfillingMealAdvancement, "a_fulfilling_meal");
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
            // If the player doesn't have the firstBloodAdvancement advancement, grant it
            Advancement firstBloodAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "first_blood"));
            player.getAdvancements().award(firstBloodAdvancement, "first_blood");

        });
    }

    public static void handleBuffToggleRequest(ServerPlayer player) {

        // Ignore if the player isn't a vampire or werewolf
        if (!"vampire".equals(VariableManager.getSpecies(player)) && !"werewolf".equals(VariableManager.getSpecies(player))) return;

        // Toggle the buffed state
        VariableManager.setBuffed(!VariableManager.isBuffed(player), player);

        if (VariableManager.isBuffed(player)) { // If the buffed state is now enabled
            player.displayClientMessage(Component.literal("§aYour movement buffs are now enabled."), true);
        } else { // If the buffed state is now disabled
            player.displayClientMessage(Component.literal("§cYour movement buffs are now disabled."), true);
        }
    }
}