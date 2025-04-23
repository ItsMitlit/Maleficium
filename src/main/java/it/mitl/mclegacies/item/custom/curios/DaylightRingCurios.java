package it.mitl.mclegacies.item.custom.curios;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DaylightRingCurios extends Item implements ICurioItem {
    public DaylightRingCurios() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }
}
