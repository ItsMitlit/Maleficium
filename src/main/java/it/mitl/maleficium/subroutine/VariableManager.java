package it.mitl.maleficium.subroutine;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.network.ModVariables;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class VariableManager {

    public static void setSpecies(String species, Entity entity) {
        if (!(entity instanceof ServerPlayer player)) return;

        entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.species = species;
            capability.syncPlayerVariables(entity);

            // Advancements
            String advancementId = switch (species.toLowerCase()) {
                case "vampire" -> "become_vampire";
                case "witch" -> "become_witch";
                case "werewolf" -> "become_werewolf";
                default -> null;
            };

            if (advancementId != null) {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, advancementId));
                    if (advancement != null) {
                        player.getAdvancements().award(advancement, advancementId);
                    }
                }
            }
        });
    }

    public static String getSpecies(Entity entity) {
        if(!(entity instanceof net.minecraft.world.entity.player.Player)) return null;

        return (entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).species;
    }

    public static void setFaction(String faction, Entity entity) {
        if (!(entity instanceof Player player)) return;

        entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.faction = faction;
            capability.syncPlayerVariables(entity);
        });
    }

    public static String getFaction(Entity entity) {
        if(!(entity instanceof net.minecraft.world.entity.player.Player)) return null;

        return (entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).faction;
    }

    public static void setBuffed(boolean value, Entity entity) {
        if (!(entity instanceof Player player)) return;

        entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.buffToggle = value;
            capability.syncPlayerVariables(entity);
        });
    }

    public static boolean isBuffed(Entity entity) {
        if(!(entity instanceof net.minecraft.world.entity.player.Player)) return false;

        return (entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).buffToggle;
    }
}
