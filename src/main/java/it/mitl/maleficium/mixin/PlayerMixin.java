package it.mitl.maleficium.mixin;

import it.mitl.maleficium.effect.ModEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3 travelVector, CallbackInfo ci) {
        if (((Player)(Object)this).hasEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravelClient(Vec3 travelVector, CallbackInfo ci) {
        if (((Player)(Object)this).hasEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get())
                && ((Player)(Object)this).level().isClientSide) {
            ci.cancel();
        }
    }
}
