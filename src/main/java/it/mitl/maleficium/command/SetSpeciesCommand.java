package it.mitl.maleficium.command;

import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Set;

@Mod.EventBusSubscriber
public class SetSpeciesCommand {

    private static final Set<String> VALID_SPECIES = Set.of("human", "vampire", "werewolf", "witch", "original_vampire");

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("setspecies")
                .then(Commands.argument("species", StringArgumentType.string())
                        .executes(arguments -> {
                            Entity entity = arguments.getSource().getEntity();
                            if (entity == null) {
                                return 0;
                            }

                            String species = arguments.getArgument("species", String.class);
                            if (!VALID_SPECIES.contains(species)) {
                                entity.sendSystemMessage(Component.literal("§8[§7Maleficium§8]§r Invalid species! Valid options are: " + String.join(", ", VALID_SPECIES)));
                                return 0;
                            }

                            VariableManager.setSpecies(species, entity);
                            entity.sendSystemMessage(Component.literal("§8[§7Maleficium§8]§r Success! You are now a §l" + species + "§r!"));
                            return 1;
                        })));
    }
}