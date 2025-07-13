package it.mitl.maleficium.subroutine;

import it.mitl.maleficium.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PlayerUtils {

    public static boolean isPlayerOnSoulSand(ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.position();
        int x = (int) Math.floor(pos.x);
        int y = (int) Math.floor(pos.y);
        int z = (int) Math.floor(pos.z);
        BlockPos blockPos = new BlockPos(x, y, z);
        return level.getBlockState(blockPos).is(BlockTags.SOUL_SPEED_BLOCKS);
    }

    public static boolean isPlayerFalling(ServerPlayer player) {
        Vec3 pos = player.position();
        int x = (int) Math.floor(pos.x);
        int y = (int) Math.floor(pos.y) - 1;
        int z = (int) Math.floor(pos.z);
        Level level = player.level();
        // Search downwards for the first block
        while (y > level.getMinBuildHeight()) {
            if (!level.getBlockState(new net.minecraft.core.BlockPos(x, y, z)).isAir()) {
                // Check if player is standing on or inside the block
                double blockTop = y + 1.0;
                return pos.y > blockTop;
            }
            y--;
        }
        // No block below
        return true;
    }

    public static void addHungerForVampire(float amount, ServerPlayer player) {
        float foodLevel = player.getFoodData().getFoodLevel();
        float remainingAmount = 0;

        if (foodLevel + amount <= 20) {
            float newHunger = player.getFoodData().getFoodLevel() + amount;
            player.getFoodData().setFoodLevel((int) newHunger);
            return;
        } else {
            remainingAmount = (foodLevel + amount) - 20;
            player.getFoodData().setFoodLevel(20);
        }

        if (foodLevel == 20) {
            int extraHunger = VariableManager.getExtraHunger(player);
            int remainingInt = (int) Math.floor(remainingAmount);

            if (extraHunger + remainingInt <= 20) {
                VariableManager.setExtraHunger(extraHunger + remainingInt, player);
            } else {
                VariableManager.setExtraHunger(20, player);
            }
        }
    }
    public static boolean shouldBeDesiccated(ServerPlayer player) {
        return VariableManager.getSpecies(player).equals("vampire")
                && player.getFoodData().getFoodLevel() <= 1;
    }

    public static void addDarkMagic(ServerPlayer player, int amount) {
        if (player == null || player.level().isClientSide()) return;
        MobEffectInstance darkMagicEffect = player.getEffect(ModEffects.WITCH_DARK_MAGIC_EFFECT.get());
        if (darkMagicEffect == null) {
            darkMagicEffect = new MobEffectInstance(ModEffects.WITCH_DARK_MAGIC_EFFECT.get(), -1, 0, false, false, true);
            player.addEffect(darkMagicEffect);
            return;
        }
        int amplifier = darkMagicEffect.getAmplifier();
        int newAmplifier = Math.min(amplifier + amount, 19);

        if (newAmplifier != amplifier) {
            player.removeEffect(ModEffects.WITCH_DARK_MAGIC_EFFECT.get());
            MobEffectInstance newEffect = new MobEffectInstance(ModEffects.WITCH_DARK_MAGIC_EFFECT.get(), -1, newAmplifier, false, false, true);
            player.addEffect(newEffect);
        }
    }
}
