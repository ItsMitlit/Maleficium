package it.mitl.maleficium.event.species.witch;

import it.mitl.maleficium.subroutine.VariableManager;
import it.mitl.maleficium.subroutine.spell.Invisique;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class WitchChatEvent {

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        if (!"witch".equals(VariableManager.getSpecies(player))) return;
        String rawMessage = event.getRawText();
        System.out.println("[Maleficium] Player " + player.getName().getString() + " sent a message: " + rawMessage);
        String lowerCaseMessage = rawMessage.toLowerCase();

        if (lowerCaseMessage.equals("invisique") || lowerCaseMessage.equals("phasmatos radium calaraa") || lowerCaseMessage.equals("nullum visa laris")) {
            Invisique.castInvisique(player, player);
            player.displayClientMessage(Component.literal("§aYou cast an Illusionary Cloaking Spell!"), true);
            System.out.println("[Maleficium] Player " + player.getName().getString() + " cast an Illusionary Cloaking Spell.");
            return;
        }// else if (lowerCaseMessage.equals(""))

    }
}
