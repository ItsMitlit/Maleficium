package it.mitl.maleficium.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BloodSuckKeybind {
    public static final KeyMapping SUCK_BLOOD_KEY = new KeyMapping(
            "key.maleficium.suck_blood",
            GLFW.GLFW_KEY_B,
            "key.categories.maleficium"
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SUCK_BLOOD_KEY);
    }
}
