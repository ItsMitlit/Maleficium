package it.mitl.maleficium.event.mobs;

import it.mitl.maleficium.capability.ModCapabilities;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class MobBloodRegenEvent {

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            mob.getCapability(ModCapabilities.BLOOD).ifPresent(blood -> {
                if (mob.tickCount % 1200 == 0) { // 1 min (ticks)
                    float currentBlood = blood.getBlood();
                    float maxBlood = blood.getMaxBlood();
                    if (currentBlood < maxBlood){
                        blood.setBlood(currentBlood + 1);
                    }
                }
            });
        }
    }
}
