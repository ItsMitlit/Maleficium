package it.mitl.mclegacies.capability.blood;

import it.mitl.mclegacies.capability.ModCapabilities;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class BloodCapability {
    public interface IBlood {
        float getBlood();
        void setBlood(float amount);
        float getMaxBlood();
        void setMaxBlood(float amount);
    }

    public static Optional<IBlood> getBloodCapability(Entity entity) {
        return entity.getCapability(ModCapabilities.BLOOD).resolve();
    }
}
