package it.mitl.maleficium.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FastTravelKeybind {
    public static final KeyMapping FAST_TRAVEL_KEY = new KeyMapping(
            "key.maleficium.fast_travel",
            GLFW.GLFW_KEY_M,
            "key.categories.maleficium"
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(FAST_TRAVEL_KEY);
    }
}
