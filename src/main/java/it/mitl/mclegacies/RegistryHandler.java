package it.mitl.mclegacies;

import it.mitl.mclegacies.capability.ModCapabilities;
import it.mitl.mclegacies.config.MCLegaciesCommonConfigs;
import it.mitl.mclegacies.event.ClientEvents;
import it.mitl.mclegacies.event.ModEvents;
import it.mitl.mclegacies.item.ModCreativeModeTabs;
import it.mitl.mclegacies.item.ModItems;
import it.mitl.mclegacies.network.ModMessages;
import it.mitl.mclegacies.network.packet.BloodSuckC2SPacket;
import it.mitl.mclegacies.network.packet.ToggleBuffedC2SPacket;
import it.mitl.mclegacies.network.packet.VillagerDiscountC2SPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {

    public static boolean isCuriosLoaded = false;

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);

        ModMessages.addNetworkMessage(
                VillagerDiscountC2SPacket.class,
                VillagerDiscountC2SPacket::toBytes,
                VillagerDiscountC2SPacket::new,
                VillagerDiscountC2SPacket::handle
        );
        ModMessages.addNetworkMessage(
                BloodSuckC2SPacket.class,
                BloodSuckC2SPacket::toBytes,
                BloodSuckC2SPacket::new,
                BloodSuckC2SPacket::handle
        );
        ModMessages.addNetworkMessage(
                ToggleBuffedC2SPacket.class,
                ToggleBuffedC2SPacket::toBytes,
                ToggleBuffedC2SPacket::new,
                ToggleBuffedC2SPacket::handle
        );

        MinecraftForge.EVENT_BUS.register(ModMessages.class);
        MinecraftForge.EVENT_BUS.addListener(ModCapabilities::registerCapabilities);
        ClientEvents.register();
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(ModEvents.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MCLegaciesCommonConfigs.SPEC, "mclegacies-common.toml");

        isCuriosLoaded = ModList.get().isLoaded("curios");


        if (ModList.get().isLoaded("curios")) {
            try {
                Class<?> clazz = Class.forName("it.mitl.mclegacies.compat.curios.CuriosCompat");
                Object method = clazz.getMethod("registerIMCEvent");
                ((Runnable) () -> {
                    try {
                        ((java.lang.reflect.Method) method).invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
