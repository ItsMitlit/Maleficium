package it.mitl.mclegacies.event.species.vampire;

import it.mitl.mclegacies.item.ModItems;
import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class VampireBurnEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) return;

        // Check if the player is a vampire
        if (!"vampire".equals(VariableManager.getSpecies(player))) return;

        // If the player is in creative or spectator mode, do not apply the effect
        if (player.isCreative() || player.isSpectator()) return;

        // Check if the player has a Daylight Ring in either hand
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        if (mainHandItem.is(ModItems.DAYLIGHT_RING.get()) || offHandItem.is(ModItems.DAYLIGHT_RING.get())) return;


        Level level = player.level();

        // Check if the player is in the overworld
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        // Check if it is daytime
        long timeOfDay = level.getDayTime() % 24000;
        if (timeOfDay < 1000 || timeOfDay > 13000) return;

        // Check if there are no blocks above the player's head
        if (level.canSeeSky(player.blockPosition())) {
            // Set the player on fire
            player.setSecondsOnFire(1);
        }
    }
}