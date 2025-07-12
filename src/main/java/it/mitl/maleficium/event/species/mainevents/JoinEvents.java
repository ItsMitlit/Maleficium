package it.mitl.maleficium.event.species.mainevents;

import it.mitl.maleficium.Maleficium;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class JoinEvents {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        MinecraftServer server = player.getServer();

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Component message = Component.literal("[").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("Maleficium").withStyle(ChatFormatting.RED))
                    .append(Component.literal("] ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal("This mod is in beta! Report bugs at ").withStyle(ChatFormatting.RED))
                    .append(
                            Component.literal("https://github.com/ItsMitlit/Maleficium")
                                    .withStyle(Style.EMPTY
                                            .withColor(ChatFormatting.BLUE)
                                            .withUnderlined(true)
                                            .withClickEvent(new ClickEvent(
                                                    ClickEvent.Action.OPEN_URL,
                                                    "https://github.com/ItsMitlit/Maleficium"
                                            ))
                                    )
                    );
            serverPlayer.sendSystemMessage(message);
        }

        if (server != null) {
            Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation(Maleficium.MOD_ID, "maleficium"));
            if (advancement != null && !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                player.getAdvancements().award(advancement, "on_join");
            }
        }
    }
}
