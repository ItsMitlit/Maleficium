package it.mitl.maleficium.effect.vampire;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VampireBloodEffect extends MobEffect {

    public VampireBloodEffect(MobEffectCategory pCategory, int pColor) {
        super (pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
