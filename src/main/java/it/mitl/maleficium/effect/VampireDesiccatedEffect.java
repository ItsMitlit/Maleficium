package it.mitl.maleficium.effect;

import it.mitl.maleficium.client.keybind.GiveUpKeybind;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.UUID;

public class VampireDesiccatedEffect extends MobEffect {

    public VampireDesiccatedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayer player) {
            player.displayClientMessage(Component.literal("§4You are desiccated. Get a player to feed you blood or press '" + GiveUpKeybind.GIVE_UP_KEY.getTranslatedKeyMessage().getString() + "' to kill yourself."), true);
            if ("vampire".equals(VariableManager.getSpecies(player)) && player.getFoodData().getFoodLevel() >= 1) {
                player.removeEffect(ModEffects.VAMPIRE_DESICCATED_EFFECT.get());
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);

        if (entity.getEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get()) != null) {
            entity.removeEffect(ModEffects.VAMPIRE_DESICCATION_EFFECT.get());
        }
        
        entity.removeEffect(MobEffects.BLINDNESS);
        MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.BLINDNESS, -1, 255, false, false, false);
        blindnessEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(blindnessEffect);

        entity.removeEffect(MobEffects.WEAKNESS);
        MobEffectInstance weaknessEffect = new MobEffectInstance(MobEffects.WEAKNESS, -1, 255, false, false, false);
        weaknessEffect.setCurativeItems(Collections.emptyList());
        entity.addEffect(weaknessEffect);

    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        entity.removeEffect(MobEffects.BLINDNESS);
        entity.removeEffect(MobEffects.WEAKNESS);
    }
}
