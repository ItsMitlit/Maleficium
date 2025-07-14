package it.mitl.maleficium.subroutine.spell;

import it.mitl.maleficium.block.ModBlocks;
import it.mitl.maleficium.subroutine.PlayerUtils;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireCreation {
    //

    @SubscribeEvent
    public static void onDogDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Wolf dog) {
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                if (!dog.isTame() && dog.getOwner() != player && !"witch".equals(VariableManager.getSpecies(player))) return;
                Level level = dog.level();
                if (level.isDay() || !(level.getMoonPhase() == 0)) return; // full moon
                // 10x10x10 area around where the dog dies
                int logCount = 0;
                int leafCount = 0;
                BlockPos deathPos = dog.blockPosition();

                for (int x = -5; x <= 5; x++) {
                    for (int y = -5; y <= 5; y++) {
                        for (int z = -5; z <= 5; z++) {
                            BlockPos checkPos = deathPos.offset(x, y, z);
                            BlockState state = level.getBlockState(checkPos);
                            if (state.getBlock() == ModBlocks.WHITE_OAK_LOG.get()) logCount++;
                            if (state.getBlock() == ModBlocks.WHITE_OAK_LEAVES.get()) leafCount++;
                        }
                    }
                }
                if (logCount < 3 || leafCount < 3) return;
                // Conditions met
                player.displayClientMessage(Component.literal("§aYou have activated the vampire creation ritual."), true);
                PlayerUtils.addDarkMagic(player, 2);
            }
        }
    }
}
