package it.mitl.mclegacies.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> MCLEGACIES_TAB = CREATIVE_MODE_TABS.register("mclegacies_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DAYLIGHT_RING.get()))
                    .title(Component.translatable("creativetab.mclegacies_tab"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(ModItems.DAYLIGHT_RING.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
