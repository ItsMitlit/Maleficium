package it.mitl.maleficium.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HearingKeybind {
    public static final KeyMapping VAMPIRE_HEARING_KEY = new KeyMapping(
            "key.maleficium.vampire_hearing",
            GLFW.GLFW_KEY_SEMICOLON,
            "key.categories.maleficium"
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(VAMPIRE_HEARING_KEY);
    }
}
