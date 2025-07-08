package it.mitl.maleficium.subroutine;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class SoundPlayer {

    public static void playBloodSuckSound(Player player) {
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_DRINK,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
}
