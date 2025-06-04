package it.mitl.maleficium.item.custom;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhiteOakStakeItem extends Item {
    public WhiteOakStakeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("§7§oCapable of killing an original vampire"));
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && "vampire".equals(VariableManager.getSpecies(player))) {
            target.kill();
            return false;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
