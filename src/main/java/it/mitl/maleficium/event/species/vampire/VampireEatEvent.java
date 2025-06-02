package it.mitl.maleficium.event.species.vampire;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireEatEvent {
    @SubscribeEvent
    public static void onPlayerEat(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) return;

        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();

        if (!"vampire".equals(VariableManager.getSpecies(player))) return;
        if (!itemStack.isEdible()) return;

        event.setCanceled(true);
        player.displayClientMessage(Component.literal("§4You can't eat items anymore. How about blood?"), true);
    }
}
