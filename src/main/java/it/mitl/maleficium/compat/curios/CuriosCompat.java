package it.mitl.maleficium.compat.curios;

import it.mitl.maleficium.item.custom.curios.DaylightRingCurios;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat {

    public static void registerIMCEvent() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosCompat::enqueueIMC);
    }

    public static void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.RING.getMessageBuilder().size(1).build());
    }

    public static Item createDaylightRing() {
        return new DaylightRingCurios();
    }

}
