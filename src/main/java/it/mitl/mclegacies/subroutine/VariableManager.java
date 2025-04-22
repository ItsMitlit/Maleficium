package it.mitl.mclegacies.subroutine;

import it.mitl.mclegacies.network.ModVariables;
import net.minecraft.client.Minecraft;
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
}
