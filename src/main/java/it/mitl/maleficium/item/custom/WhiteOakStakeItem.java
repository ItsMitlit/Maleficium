package it.mitl.maleficium.item.custom;

import it.mitl.maleficium.subroutine.SpeciesCheck;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhiteOakStakeItem extends SwordItem {
    public WhiteOakStakeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("§7§oCapable of killing an original vampire"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity targetEntity) {
        if (player instanceof Player && targetEntity instanceof Player && SpeciesCheck.isOriginalVampire((Player) targetEntity)) {
            LevelAccessor world = player.level();
            System.out.println("White Oak Stake used on original vampire: " + targetEntity.getName().getString());
            targetEntity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("maleficium:white_oak_stake_death")))), 3.4028235E38F);
            return false;
        }
        System.out.println("White Oak Stake used on non-original vampire or non-player entity: " + targetEntity.getName().getString());
        return super.onLeftClickEntity(stack, player, targetEntity);
    }

}
