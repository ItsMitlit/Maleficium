package it.mitl.mclegacies.capability;

import it.mitl.mclegacies.capability.blood.BloodCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilities {
    public static Capability<BloodCapability.IBlood> BLOOD = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(BloodCapability.IBlood.class);
    }
}
