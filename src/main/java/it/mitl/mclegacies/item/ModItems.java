package it.mitl.mclegacies.item;

import it.mitl.mclegacies.RegistryHandler;
import it.mitl.mclegacies.item.custom.DaylightRingItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> DAYLIGHT_RING = ITEMS.register("daylight_ring", () -> {
        if (RegistryHandler.isCuriosLoaded) {
            try {
                // Yucky mess because it will crash if Curios is not loaded
                return (Item) Class.forName("it.mitl.mclegacies.compat.curios.CuriosCompat")
                        .getMethod("createDaylightRing")
                        .invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DaylightRingItem();
    });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
