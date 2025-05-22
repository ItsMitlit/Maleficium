package it.mitl.maleficium.capability.blood;

import it.mitl.maleficium.capability.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class BloodAttacher {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof LivingEntity living)) return;
        if (living.getMobType() == MobType.UNDEAD) return; // Skip undead

        event.addCapability(new ResourceLocation(MOD_ID, "blood"), new ICapabilityProvider() {
            final BloodCapabilityImpl blood = new BloodCapabilityImpl();
            final LazyOptional<BloodCapability.IBlood> opt = LazyOptional.of(() -> blood);

            {
                if (living instanceof Villager) {
                    blood.setMaxBlood(20);
                    blood.setBlood(20);
                } else {
                    blood.setMaxBlood(5);
                    blood.setBlood(5);
                }
            }

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                return cap == ModCapabilities.BLOOD ? opt.cast() : LazyOptional.empty();
            }
        });
    }
}
