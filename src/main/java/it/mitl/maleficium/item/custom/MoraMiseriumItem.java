package it.mitl.maleficium.item.custom;

import it.mitl.maleficium.subroutine.PlayerUtils;
import it.mitl.maleficium.subroutine.SoundPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoraMiseriumItem extends Item {
    private static final String OWNER_TAG = "owner";
    private static final String DARK_MAGIC_TAG = "darkmagic";

    private static final int MAX_DARK_MAGIC = 100;

    public MoraMiseriumItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .durability(500)
                .fireResistant());
    }

    public static void setOwner(ItemStack stack, String owner) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(OWNER_TAG, owner);
    }

    /**
     * Sets the dark magic value for the item.
     *
     * @param stack     The ItemStack to modify.
     * @param DarkMagic The value to use for dark magic.
     * @param method    The operation to perform:
     *                  0 = set to the given value,
     *                  1 = add to current value,
     *                  2 = subtract from current value.
     *
     */
    public static void setItemDarkMagic(ItemStack stack, int DarkMagic, int method) {
        CompoundTag tag = stack.getOrCreateTag();
        int currentDarkMagic = tag.getInt(DARK_MAGIC_TAG);
        int newDarkMagic = currentDarkMagic;
        newDarkMagic = switch (method) {
            case 0 -> DarkMagic;
            case 1 -> Math.min(100, currentDarkMagic + DarkMagic);
            case 2 -> Math.max(0, currentDarkMagic - DarkMagic);
            default -> newDarkMagic;
        };
        tag.putInt(DARK_MAGIC_TAG, newDarkMagic);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if (level.isClientSide) return InteractionResultHolder.pass(stack);

        if (player.isCrouching() && !tag.contains(OWNER_TAG)) {
            setOwner(stack, player.getName().getString());
            setItemDarkMagic(stack, 0, 2);
            player.displayClientMessage(Component.literal("§5This Mora Miserium is now bound to you."), true);
            SoundPlayer.playXpSound(player);
        } else if (player.isCrouching() && tag.contains(OWNER_TAG) && tag.getString(OWNER_TAG).equals(player.getName().getString()) && PlayerUtils.getDarkMagic((ServerPlayer) player) != -1) {
            if (tag.getInt(DARK_MAGIC_TAG) >= MAX_DARK_MAGIC) {
                player.displayClientMessage(Component.literal("§cThis Mora Miserium is already full of §5dark magic."), true);
                SoundPlayer.playXpSound(player);
                return InteractionResultHolder.pass(stack);
            }
            player.displayClientMessage(Component.literal("§5You have stored some dark magic in the Mora Miserium."), true);
            SoundPlayer.playXpSound(player);
            PlayerUtils.setDarkMagic((ServerPlayer) player, 1, 2);
            setItemDarkMagic(stack, 1, 1);
        } else if (player.isCrouching() && tag.contains(OWNER_TAG) && tag.getString(OWNER_TAG).equals(player.getName().getString()) && PlayerUtils.getDarkMagic((ServerPlayer) player) == -1) {
            player.displayClientMessage(Component.literal("§cYou have no §5dark magic§c to store in the Mora Miserium."), true);
            SoundPlayer.playXpSound(player);
        } else if (!tag.contains(OWNER_TAG) || !tag.getString(OWNER_TAG).equals(player.getName().getString())) {
            player.displayClientMessage(Component.literal("§cThis Mora Miserium is not bound to you."), true);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("§7§oUsed to hold §5dark magic"));

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(OWNER_TAG)) {
            String owner = tag.getString(OWNER_TAG);
            tooltip.add(Component.literal("§8Owner: " + owner));
        }
        if (tag != null && tag.contains(DARK_MAGIC_TAG)) {
            int darkMagic = tag.getInt(DARK_MAGIC_TAG);
            tooltip.add(Component.literal("§5Dark Magic: " + darkMagic + "/" + MAX_DARK_MAGIC));
        }
    }
}
