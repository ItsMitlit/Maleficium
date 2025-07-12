package it.mitl.maleficium.effect;

import it.mitl.maleficium.Maleficium;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Maleficium.MOD_ID);

    public static final RegistryObject<MobEffect> VAMPIRIC_TRANSITION_EFFECT = MOB_EFFECTS.register("vampiric_transition",
            () -> new VampiricTransitionEffect(MobEffectCategory.NEUTRAL, 0x808080));
    public static final RegistryObject<MobEffect> VAMPIRE_BLOOD_EFFECT = MOB_EFFECTS.register("vampire_blood",
            () -> new VampireBloodEffect(MobEffectCategory.NEUTRAL, 0x931c1c));
    public static final RegistryObject<MobEffect> VAMPIRE_DESICCATION_EFFECT = MOB_EFFECTS.register("vampire_desiccation",
            () -> new VampireDesiccationEffect(MobEffectCategory.HARMFUL, 0x000000));
    public static final RegistryObject<MobEffect> VAMPIRE_DESICCATED_EFFECT = MOB_EFFECTS.register("vampire_desiccated",
            () -> new VampireDesiccatedEffect(MobEffectCategory.HARMFUL, 0x000000));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
