package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.subroutine.SpeciesCheck;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireFoodEvent {
    @SubscribeEvent
    public static void onPlayerEatAttempt(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) return;

        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();

        if (!SpeciesCheck.isAnyVampire(player)) return;
        if (!itemStack.isEdible()) return;

        event.setCanceled(true);
        player.displayClientMessage(Component.literal("§4You can't eat items anymore. How about blood?"), true);
    }

    @SubscribeEvent
    public static void onPlayerHungerDecreaseTick(TickEvent.PlayerTickEvent event) {

        Player player = event.player;
        if (!SpeciesCheck.isAnyVampire(player)) return;

        if (player.getFoodData().getFoodLevel() < 20 && VariableManager.getExtraHunger(player) > 0) {
            int extraHunger = VariableManager.getExtraHunger(player);
            int foodLevel = player.getFoodData().getFoodLevel();
            int hungerToFill = Math.min(20 - foodLevel, extraHunger);

            player.getFoodData().setFoodLevel(foodLevel + hungerToFill);
            VariableManager.setExtraHunger(extraHunger - hungerToFill, player);
        }
    }
}
