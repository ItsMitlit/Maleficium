package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.config.MaleficiumCommonConfigs;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.UUID;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireAttributeEvent {

    // UUID generator: https://www.uuidtools.com/minecraft
    private static final UUID STRENGTH_MODIFIER_UUID = UUID.fromString("07121894-24e4-49d2-9d75-d9e69ce9d0b8");
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("bb91da3f-c963-46b6-ad8c-43496a5dab3f");
    public static final UUID SPEED_MODIFIER_UUID = UUID.fromString("19ea251b-9560-49b2-881d-cb11f62e95a1");
    public static final UUID JUMP_MODIFIER_UUID = UUID.fromString("730b2a9f-62bd-4319-a789-225b12708bcf");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) return;

        // Check if the player is a vampire
        boolean isVampire = "vampire".equals(VariableManager.getSpecies(player));
        boolean isHuman = "human".equals(VariableManager.getSpecies(player));

        // Strength Modifier
        if (isVampire && player.getHealth() > 1.0F) { // Only apply strength if the player is a vampire and has more than 1/2 a heart of health
            if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(STRENGTH_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                        new AttributeModifier(STRENGTH_MODIFIER_UUID, "Vampire strength boost", MaleficiumCommonConfigs.VAMPIRE_STRENGTH_MULTIPLIER.get(), AttributeModifier.Operation.MULTIPLY_TOTAL)
                );
            }
        } else {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(STRENGTH_MODIFIER_UUID);
        }

        // Health Modifier
        if (isVampire) { // Only apply health boost if the player is a vampire and has more than 1/2 a heart of health
            if (player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEALTH_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                        new AttributeModifier(HEALTH_MODIFIER_UUID, "Vampire health boost", MaleficiumCommonConfigs.VAMPIRE_HEALTH_INCREASE.get(), AttributeModifier.Operation.ADDITION)
                );
                player.setHealth(player.getMaxHealth()); // Ensure the health updates instantly
            }
        } else {
            if (player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEALTH_MODIFIER_UUID) != null) {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_MODIFIER_UUID);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth()); // Ensure the health updates instantly
                }
            }
        }

        // Speed Modifier
        if (!isHuman && VariableManager.isBuffed(player) && player.getHealth() > 1.0F) { // Only apply speed boost if the player is a vampire, buffed, and has more than 1/2 a heart of health
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                        new AttributeModifier(SPEED_MODIFIER_UUID, "Vampire speed boost", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL)
                );
            }
        } else if (player.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPEED_MODIFIER_UUID);
        }

        // Near death negative effects
        if (isVampire && player.getHealth() <= 1.0F) {
            applyEffectIfMissing(player, MobEffects.MOVEMENT_SLOWDOWN, 60, 1);
            applyEffectIfMissing(player, MobEffects.WEAKNESS, 60, 1);
            applyEffectIfMissing(player, MobEffects.DIG_SLOWDOWN, 60, 1);
            applyEffectIfMissing(player, MobEffects.BLINDNESS, 60, 0);
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!"vampire".equals(VariableManager.getSpecies(player))) return;
            if (!VariableManager.isBuffed(player) || player.getHealth() <= 1.0F) return;
            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.2, 0)); // up, up, and away
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!"vampire".equals(VariableManager.getSpecies(player))) return;
            if (!VariableManager.isBuffed(player) || player.getHealth() <= 1.0F) return;
            //event.setDistance(event.getDistance() * 0.7f); // reduce fall damage by 30% to try account for the jump boost
            event.setDistance(event.getDistance() - 1.5f); // reduce fall damage by 1 and a half blocks.
        }
    }

    private static void applyEffectIfMissing(Player player, MobEffect effect, int duration, int amplifier) {
        MobEffectInstance currentEffect = player.getEffect(effect);
        if (currentEffect == null || currentEffect.getDuration() < duration - 1) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, true, false));
        }
    }
}
