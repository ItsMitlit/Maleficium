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

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
