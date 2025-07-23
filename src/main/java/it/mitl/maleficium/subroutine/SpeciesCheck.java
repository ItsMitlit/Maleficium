package it.mitl.maleficium.subroutine;

import net.minecraft.world.entity.player.Player;

public class SpeciesCheck {

    public static boolean isOnlyVampire(Player player) {
        return "vampire".equals(VariableManager.getSpecies(player));
    }

    public static boolean isAnyVampire(Player player) {
        String species = VariableManager.getSpecies(player);
        return "vampire".equals(species) || "original_vampire".equals(species);
    }

    public static boolean isOriginalVampire(Player player) {
        return "original_vampire".equals(VariableManager.getSpecies(player));
    }

    public static boolean isWerewolf(Player player) {
        return "werewolf".equals(VariableManager.getSpecies(player));
    }

    public static boolean isWitch(Player player) {
        return "witch".equals(VariableManager.getSpecies(player));
    }
}
