package it.mitl.maleficium.command;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KarmaCommand {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("karma")
                .executes(
                        context -> {
                            Player player = context.getSource().getPlayerOrException();
                            int karma = VariableManager.getKarma(player);

                            String karmaTitle = "Neutral";
                            String karmaColor = "§7";

                            if (karma >= 250) {
                                karmaTitle = "Saviour";
                                karmaColor = "§6";
                            } else if (karma >= 100) {
                                karmaTitle = "Hero";
                                karmaColor = "§e";
                            } else if (karma >= 51) {
                                karmaTitle = "Good Samaritan";
                                karmaColor = "§2";
                            } else if (karma > 0) {
                                karmaTitle = "Good";
                                karmaColor = "§a";
                            } else if (karma == 0) {
                                karmaTitle = "Neutral";
                                karmaColor = "§7";
                            } else if (karma > -50) {
                                karmaTitle = "Bad";
                                karmaColor = "§c";
                            } else if (karma > -100) {
                                karmaTitle = "Evildoer";
                                karmaColor = "§4";
                            } else if (karma > -250) {
                                karmaTitle = "Villain";
                                karmaColor = "§d";
                            } else {
                                karmaTitle = "Evil Incarnate";
                                karmaColor = "§5";
                            }

                            player.sendSystemMessage(Component.literal("§8[§7Maleficium§8]§r Your current karma is: " + karmaColor + "§l" + karma + "§r (" + karmaColor + karmaTitle + "§r)"));
                            return 1;
                            // [Maleficium] Your current karma is: 100 (Hero)
                        }
                )
        );
    }
}
