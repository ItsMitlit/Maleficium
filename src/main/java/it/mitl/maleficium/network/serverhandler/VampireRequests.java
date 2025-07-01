package it.mitl.maleficium.network.serverhandler;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.capability.blood.BloodCapability;
import it.mitl.maleficium.subroutine.FollowEntityGoal;
import it.mitl.maleficium.subroutine.PlayerUtils;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class VampireRequests {

    public static void handleFastTravelRequest(ServerPlayer player) {
        // Teleport the player to the block they are looking at (within 100 blocks)
        if (!"vampire".equals(VariableManager.getSpecies(player))) {
            return; // Player isn't a vampire
        }
        FoodData foodData = player.getFoodData();
        if (foodData.getFoodLevel() < 8) {
            player.displayClientMessage(Component.literal("§4You are too hungry to use this ability!"), true);
            return; // Player is too hungry
        }

        if (player.isFallFlying() || PlayerUtils.isPlayerFalling(player) || player.isInWater() || player.isInLava() || player.isPassenger() || player.isVehicle()
                || player.isSleeping() || player.isSpectator() || player.isOnFire() || player.isInPowderSnow || PlayerUtils.isPlayerOnSoulSand(player)) {
            String message = "§4You can't use this ability while ";
            if (PlayerUtils.isPlayerOnSoulSand(player)) message += "on soul sand!";
            else if (player.isFallFlying() || PlayerUtils.isPlayerFalling(player)) message += "falling!";
            else if (player.isInWater() || player.isInLava()) message += "in water or lava!";
            else if (player.isPassenger() || player.isVehicle()) message += "riding an entity!";
            else if (player.isSleeping()) message += "sleeping!";
            else if (player.isSpectator()) message += "in spectator mode!";
            else if (player.isOnFire()) message += "on fire!";
            else if (player.isInPowderSnow) message += "in powder snow!";
            player.displayClientMessage(Component.literal(message), true);
            return;
        }
        // Get the block the player is looking at
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getViewVector(1.0F).scale(100);

        HitResult hitResult = player.level().clip(new ClipContext(
                eyePosition,
                eyePosition.add(lookVector),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 targetPosition = hitResult.getLocation();
            Vec3 startPosition = player.position();

            // Create a trail of particles
            double distance = startPosition.distanceTo(targetPosition);
            int particleCount = (int) (distance * 10); // Density
            for (int i = 0; i <= particleCount; i++) {
                double progress = i / (double) particleCount;
                double x = startPosition.x + (targetPosition.x - startPosition.x) * progress;
                double y = startPosition.y + (targetPosition.y - startPosition.y) * progress;
                double z = startPosition.z + (targetPosition.z - startPosition.z) * progress;

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SMOKE,
                            x, y, z,
                            1,
                            0, 0, 0,
                            0.0
                    );
                }
            }

            // Teleport the player
            // player.displayClientMessage(Component.literal("New Coordinates: " + targetPosition), true);
            player.teleportTo(targetPosition.x(), targetPosition.y(), targetPosition.z());
            foodData.setFoodLevel(foodData.getFoodLevel() - 8); // Reduce hunger by 8 points (4 blood)
        }
    }

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

        // Don't let the player suck blood from mobs with over 20 health
        if (entity instanceof LivingEntity livingEntity && livingEntity.getMaxHealth() > 40.0f) {
            player.displayClientMessage(Component.literal("§4You can't suck blood from this mob!"), true);
            return;
        }

        // Don't let the player suck blood from other vampires
        if (entity instanceof Player entityPlayer && ("vampire".equals(VariableManager.getSpecies(entityPlayer)))) {
            player.displayClientMessage(Component.literal("§4You can't suck blood from another vampire!"), true);
            return;
        }

        BloodCapability.getBloodCapability(entity).ifPresent(blood -> {
            float currentBlood = blood.getBlood();
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 255, true, false));
            }
            if (entity instanceof Villager villager) {
                if (currentBlood <= 2) {
                    entity.kill();
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, true, false)); // 100 ticks = 5 seconds
                    player.displayClientMessage(Component.literal("§4You drained the villager to death!"), true);
                    Advancement aFulfillingMealAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "a_fulfilling_meal"));
                    Advancement enemyOfTheVillageAdvancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "enemy_of_the_village"));
                    player.getAdvancements().award(aFulfillingMealAdvancement, "a_fulfilling_meal");
                    player.getAdvancements().award(enemyOfTheVillageAdvancement, "enemy_of_the_village");

                    // Anger nearby golems in a 25 block radius
                    AABB searchArea = new AABB(villager.blockPosition()).inflate(25);
                    player.level().getEntitiesOfClass(IronGolem.class, searchArea, golem -> golem.isAlive())
                            .forEach(golem -> golem.setTarget(player));
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
