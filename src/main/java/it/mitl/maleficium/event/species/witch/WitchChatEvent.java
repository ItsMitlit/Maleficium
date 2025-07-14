package it.mitl.maleficium.event.species.witch;

import it.mitl.maleficium.subroutine.VariableManager;
import it.mitl.maleficium.subroutine.spell.Invisique;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static it.mitl.maleficium.Maleficium.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class WitchChatEvent {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        if (!"witch".equals(VariableManager.getSpecies(player))) return;
        String rawMessage = event.getRawText();
        LOGGER.info("[Maleficium] Player {} sent a message: {}", player.getName().getString(), rawMessage);
        String lowerCaseMessage = rawMessage.toLowerCase();

        if (lowerCaseMessage.equals("invisique") || lowerCaseMessage.equals("phasmatos radium calaraa") || lowerCaseMessage.equals("nullum visa laris")) {
            Invisique.castInvisique(player, player);
            player.displayClientMessage(Component.literal("§aYou cast an Illusionary Cloaking Spell!"), true);
            LOGGER.info("[Maleficium] Player {} cast an Illusionary Cloaking Spell.", player.getName().getString());
            return;
        }// else if (lowerCaseMessage.equals(""))

    }
}
