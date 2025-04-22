package it.mitl.mclegacies.event;

import it.mitl.mclegacies.MCLegacies;
import it.mitl.mclegacies.client.ExperienceBarColourChanger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = MCLegacies.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ClientEvents::onClientSetup);

        MinecraftForge.EVENT_BUS.register(ExperienceBarColourChanger.class);
    }

    public static void onClientSetup(final FMLClientSetupEvent event) {
        // Client setup code can go here
    }
}
