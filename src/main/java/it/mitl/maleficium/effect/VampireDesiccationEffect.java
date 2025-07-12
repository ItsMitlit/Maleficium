package it.mitl.maleficium.effect;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;


public class VampireDesiccationEffect extends MobEffect {

    public VampireDesiccationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);

        entity.removeEffect(MobEffects.BLINDNESS);
        MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false);
        blindnessEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(blindnessEffect);

        entity.removeEffect(MobEffects.WEAKNESS);
        MobEffectInstance weaknessEffect = new MobEffectInstance(MobEffects.WEAKNESS, -1, 0, false, false, false);
        weaknessEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(weaknessEffect);

        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        MobEffectInstance movementSlowdownEffect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 1, false, false, false);
        movementSlowdownEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(movementSlowdownEffect);

        entity.removeEffect(MobEffects.DIG_SLOWDOWN);
        MobEffectInstance digSlowdownEffect = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, -1, 1, false, false, false);
        digSlowdownEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(digSlowdownEffect);

    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        entity.removeEffect(MobEffects.BLINDNESS);
        entity.removeEffect(MobEffects.WEAKNESS);
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        entity.removeEffect(MobEffects.DIG_SLOWDOWN);

        if (entity instanceof Player player) {
            if ("vampire".equals(VariableManager.getSpecies(player)) && player.getFoodData().getFoodLevel() >= 1) return;
        }
        MobEffectInstance desiccatedEffect = new MobEffectInstance(ModEffects.VAMPIRE_DESICCATED_EFFECT.get(), -1, 0, false, false, true);
        desiccatedEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(desiccatedEffect);
    }

}