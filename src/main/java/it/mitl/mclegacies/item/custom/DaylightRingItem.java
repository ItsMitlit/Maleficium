package it.mitl.mclegacies.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DaylightRingItem extends Item {
    public DaylightRingItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    // stuff goes here :D
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("§7§oCompatible with Curios API"));
    }
}
