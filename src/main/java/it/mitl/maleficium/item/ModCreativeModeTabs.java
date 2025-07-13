package it.mitl.maleficium.item;

import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static it.mitl.maleficium.Maleficium.MOD_ID;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> MALEFICIUM_TAB = CREATIVE_MODE_TABS.register("maleficium_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DAYLIGHT_RING.get()))
                    .title(Component.translatable("creativetab.maleficium_tab"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(ModItems.DAYLIGHT_RING.get());
                        output.accept(ModItems.WHITE_OAK_STAKE.get());

                        output.accept(ModBlocks.WHITE_OAK_LOG.get());
                        output.accept(ModBlocks.WHITE_OAK_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_WHITE_OAK_LOG.get());
                        output.accept(ModBlocks.STRIPPED_WHITE_OAK_WOOD.get());
                        output.accept(ModBlocks.WHITE_OAK_PLANKS.get());
                        output.accept(ModBlocks.WHITE_OAK_LEAVES.get());
                        output.accept(ModBlocks.WHITE_OAK_SAPLING.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
