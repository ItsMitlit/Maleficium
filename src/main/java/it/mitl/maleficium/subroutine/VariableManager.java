package it.mitl.maleficium.subroutine;

import it.mitl.maleficium.network.ModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class VariableManager {

    public static void setSpecies(String species, Entity entity) {
        if (!(entity instanceof Player player)) return;

        entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.species = species;
            capability.syncPlayerVariables(entity);
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
