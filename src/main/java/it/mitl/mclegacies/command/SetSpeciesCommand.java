package it.mitl.mclegacies.command;

import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Set;

@Mod.EventBusSubscriber
public class SetSpeciesCommand {

    private static final Set<String> VALID_SPECIES = Set.of("human", "vampire", "werewolf", "witch");

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
                                return 0;
                            }

                            VariableManager.setSpecies(species, entity);
                            return 1;
                        })));
    }
}