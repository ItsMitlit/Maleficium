package it.mitl.maleficium.mixin;

import it.mitl.maleficium.client.keybind.HearingKeybind;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class VampireHearingMixin {
    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void showGlowing(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && "vampire".equals(VariableManager.getSpecies(mc.player))) {
            boolean keyHeld = isVampireHearingKeyHeld();
            if (keyHeld) {
                if (!mc.player.hasEffect(MobEffects.BLINDNESS)) {
                    mc.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 167, false, false, false));
                }
                if (!mc.player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    mc.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 1, false, false, false));
                }
            } else {
                if (mc.player.hasEffect(MobEffects.BLINDNESS)) {
                    MobEffectInstance blindnessEffect = mc.player.getEffect(MobEffects.BLINDNESS);
                    if (blindnessEffect.getAmplifier() == 167) {
                        mc.player.removeEffect(MobEffects.BLINDNESS);
                        mc.player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    }
                }
            }

            if (
                    keyHeld &&
                            (
                                    (entity instanceof Mob) ||
                                            (entity instanceof Player && entity != mc.player)
                            ) &&
                            mc.player.distanceTo(entity) <= 32
            ) {
                cir.setReturnValue(true);
            }
        }
    }

    private boolean isVampireHearingKeyHeld() {
        return HearingKeybind.VAMPIRE_HEARING_KEY.isDown();
    }
}
