package it.mitl.mclegacies.event;

import it.mitl.mclegacies.MCLegacies;
import it.mitl.mclegacies.client.keybind.BloodSuckKeybind;
import it.mitl.mclegacies.client.keybind.CompulsionKeybind;
import it.mitl.mclegacies.client.ExperienceBarColourChanger;
import it.mitl.mclegacies.client.keybind.ToggleBuffKeybind;
import it.mitl.mclegacies.network.ModMessages;
import it.mitl.mclegacies.network.packet.BloodSuckC2SPacket;
import it.mitl.mclegacies.network.packet.ToggleBuffedC2SPacket;
import it.mitl.mclegacies.network.packet.VillagerDiscountC2SPacket;
import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = MCLegacies.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ClientEvents::onClientSetup);

        MinecraftForge.EVENT_BUS.register(ExperienceBarColourChanger.class);
    }

    public static void onClientSetup(final FMLClientSetupEvent event) {
        // Client setup code can go here
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        CompulsionKeybind.register(event);
    }

    private static long lastBloodSuckTime = 0;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return;

        if (CompulsionKeybind.COMPEL_KEY.consumeClick()) {
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();
                if (target instanceof Villager villager) {
                    // Send packet to server to request discount
                    ModMessages.sendToServer(new VillagerDiscountC2SPacket(villager.getUUID()));
                }
            }
        }
        if (BloodSuckKeybind.SUCK_BLOOD_KEY.isDown()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBloodSuckTime >= 500) { // 0.5 second cooldown
                HitResult hit = Minecraft.getInstance().hitResult;
                if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                    Entity target = ((EntityHitResult) hit).getEntity();
                    // Send packet to server to request blood suck.
                    ModMessages.sendToServer(new BloodSuckC2SPacket(target.getUUID()));
                }
                lastBloodSuckTime = currentTime;
            }
        }
        if (ToggleBuffKeybind.TOGGLE_BUFF_KEY.consumeClick()) {
            ModMessages.sendToServer(new ToggleBuffedC2SPacket());
        }
    }
}
