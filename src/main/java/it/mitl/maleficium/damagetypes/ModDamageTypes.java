package it.mitl.maleficium.damagetypes;

import it.mitl.maleficium.Maleficium;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModDamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, Maleficium.MOD_ID);

    public static final RegistryObject<DamageType> GAVE_UP = DAMAGE_TYPES.register(
            "gave_up",
            () -> new DamageType("maleficium.gave_up", 0.0F)
    );

    public static void register(IEventBus eventBus) {
        DAMAGE_TYPES.register(eventBus);
    }
}
