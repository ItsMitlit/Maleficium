package it.mitl.maleficium.effect.witch;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class WitchDarkMagicEffect extends MobEffect {
    private UUID EFFECT_UUID = UUID.fromString("bc5bf37f-cde1-42e2-afcc-ceff601baf1e");

    public WitchDarkMagicEffect(MobEffectCategory pCategory, int pColor) {
        super (pCategory, pColor);

        this.addAttributeModifier(
                Attributes.MAX_HEALTH,
                EFFECT_UUID.toString(),
                -1.0D,
                AttributeModifier.Operation.ADDITION
        );
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        double newMax = entity.getMaxHealth();
        if (entity.getHealth() > newMax) {
            entity.setHealth((float) newMax);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return -1.0D * (amplifier + 1);
    }

}
