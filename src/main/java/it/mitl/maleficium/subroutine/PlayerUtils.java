package it.mitl.maleficium.subroutine;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
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
}
