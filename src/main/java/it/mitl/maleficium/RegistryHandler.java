package it.mitl.maleficium;

import it.mitl.maleficium.block.ModBlocks;
import it.mitl.maleficium.capability.ModCapabilities;
import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.damagetypes.ModDamageTypes;
import it.mitl.maleficium.effect.ModEffects;
import it.mitl.maleficium.event.ClientEvents;
import it.mitl.maleficium.event.ModEvents;
import it.mitl.maleficium.item.ModCreativeModeTabs;
import it.mitl.maleficium.item.ModItems;
import it.mitl.maleficium.network.ModMessages;
import it.mitl.maleficium.network.packet.*;
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
        ModMessages.addNetworkMessage(
                EntityCompulsionC2SPacket.class,
                EntityCompulsionC2SPacket::toBytes,
                EntityCompulsionC2SPacket::new,
                EntityCompulsionC2SPacket::handle
        );
        ModMessages.addNetworkMessage(
                FastTravelC2SPacket.class,
                FastTravelC2SPacket::toBytes,
                FastTravelC2SPacket::new,
                FastTravelC2SPacket::handle
        );
        ModMessages.addNetworkMessage(
                FeedBloodC2SPacket.class,
                FeedBloodC2SPacket::toBytes,
                FeedBloodC2SPacket::new,
                FeedBloodC2SPacket::handle
        );
        ModMessages.addNetworkMessage(
                GiveUpC2SPacket.class,
                GiveUpC2SPacket::toBytes,
                GiveUpC2SPacket::new,
                GiveUpC2SPacket::handle
        );

        MinecraftForge.EVENT_BUS.register(ModMessages.class);
        MinecraftForge.EVENT_BUS.addListener(ModCapabilities::registerCapabilities);
        ClientEvents.register();
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEffects.register(modEventBus);
        // ModDamageTypes.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(ModEvents.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MaleficiumCommonConfigs.SPEC, "maleficium-common.toml");

        isCuriosLoaded = ModList.get().isLoaded("curios");


        if (ModList.get().isLoaded("curios")) {
            try {
                Class<?> clazz = Class.forName("it.mitl.maleficium.compat.curios.CuriosCompat");
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
