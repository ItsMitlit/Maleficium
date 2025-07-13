package it.mitl.maleficium.subroutine.spell;

import it.mitl.maleficium.effect.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Collections;

public class Invisique {

    private static final String NBT_KEY = "MaleficiumInvisiqueSpell";


    public static void castInvisique(ServerPlayer casterPlayer, ServerPlayer targetPlayer) {

        if (isAffected(targetPlayer)) {
            casterPlayer.displayClientMessage(Component.literal("§cThis player is already under the effects of an Illusionary Cloaking Spell!"), true);
            return;
        }

        // Give the invisibility effect
        MobEffectInstance giveEffect = new MobEffectInstance(MobEffects.INVISIBILITY, (20 * 120), 0, false, false, true);
        giveEffect.setCurativeItems(Collections.emptyList());
        targetPlayer.addEffect(giveEffect);

        CompoundTag tag = targetPlayer.getPersistentData();
        tag.putBoolean(NBT_KEY, true);
    }

    public static void removeInvisique(ServerPlayer targetPlayer) {
        targetPlayer.removeEffect(MobEffects.INVISIBILITY);
        CompoundTag tag = targetPlayer.getPersistentData();
        tag.remove(NBT_KEY);
    }

    public static boolean isAffected(ServerPlayer player) {
        return player.getPersistentData().getBoolean(NBT_KEY);
    }
}
