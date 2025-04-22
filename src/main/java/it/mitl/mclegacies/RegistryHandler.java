package it.mitl.mclegacies;

import it.mitl.mclegacies.event.ClientEvents;
import it.mitl.mclegacies.event.ModEvents;
import it.mitl.mclegacies.network.ModMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(ModMessages.class);
        ClientEvents.register();
        MinecraftForge.EVENT_BUS.register(ModEvents.class);

    }
}
