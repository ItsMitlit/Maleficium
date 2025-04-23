package it.mitl.mclegacies.event.species.vampire;

import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireAttributeEvent {

    // UUID generator: https://www.uuidtools.com/minecraft
    private static final UUID STRENGTH_MODIFIER_UUID = UUID.fromString("07121894-24e4-49d2-9d75-d9e69ce9d0b8");
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("bb91da3f-c963-46b6-ad8c-43496a5dab3f");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) return;

        // Check if the player is a vampire
        boolean isVampire = "vampire".equals(VariableManager.getSpecies(player));

        // Strength Modifier
        if (isVampire) {
            if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(STRENGTH_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                        new AttributeModifier(STRENGTH_MODIFIER_UUID, "Vampire strength boost", 5.0, AttributeModifier.Operation.ADDITION)
                );
            }
        } else {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(STRENGTH_MODIFIER_UUID);
        }

        // Health Modifier
        if (isVampire) {
            if (player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEALTH_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                        new AttributeModifier(HEALTH_MODIFIER_UUID, "Vampire health boost", 10.0, AttributeModifier.Operation.ADDITION)
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
    }
}
