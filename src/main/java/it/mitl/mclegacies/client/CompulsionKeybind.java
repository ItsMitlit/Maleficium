package it.mitl.mclegacies.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompulsionKeybind {
    public static final KeyMapping COMPEL_KEY = new KeyMapping(
            "key.mclegacies.compel",
            GLFW.GLFW_KEY_V,
            "key.categories.mclegacies"
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(COMPEL_KEY);
    }
}
