package it.mitl.maleficium.item;

import it.mitl.maleficium.RegistryHandler;
import it.mitl.maleficium.item.custom.DaylightRingItem;
import it.mitl.maleficium.item.custom.WhiteOakStakeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static it.mitl.maleficium.Maleficium.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> DAYLIGHT_RING = ITEMS.register("daylight_ring", () -> {
        if (RegistryHandler.isCuriosLoaded) {
            try {
                // Yucky mess because it will crash if Curios is not loaded
                return (Item) Class.forName("it.mitl.maleficium.compat.curios.CuriosCompat")
                        .getMethod("createDaylightRing")
                        .invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DaylightRingItem();
    });

    public static final RegistryObject<Item> WHITE_OAK_STAKE = ITEMS.register("white_oak_stake",
            () -> new WhiteOakStakeItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
