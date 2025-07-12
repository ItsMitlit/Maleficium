package it.mitl.maleficium.event;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.client.keybind.*;
import it.mitl.maleficium.client.ExperienceBarColourChanger;
import it.mitl.maleficium.network.ModMessages;
import it.mitl.maleficium.network.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = Maleficium.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        BloodSuckKeybind.register(event);
        ToggleBuffKeybind.register(event);
        FastTravelKeybind.register(event);
        FeedBloodKeybind.register(event);
        GiveUpKeybind.register(event);
    }

    private static long lastBloodSuckTime = 0;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return;

        if (CompulsionKeybind.COMPEL_KEY.consumeClick()) {
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();
                boolean isCrouching = Minecraft.getInstance().player.isCrouching();
                if (target instanceof Villager villager && isCrouching) {
                    // Send packet to server to request discount
                    ModMessages.sendToServer(new VillagerDiscountC2SPacket(villager.getUUID()));
                } else if (!(target instanceof AbstractClientPlayer) && (target instanceof LivingEntity)) {
                    if (((LivingEntity) target).getMaxHealth() >= 40.0f) {
                        Minecraft.getInstance().player.displayClientMessage(Component.literal("§4This mob is too powerful for your compulsion!"), true);
                        return;
                    }
                    ModMessages.sendToServer(new EntityCompulsionC2SPacket(target.getUUID()));
                }
            }
        }
        if (BloodSuckKeybind.SUCK_BLOOD_KEY.isDown()) {
            long currentTime = System.currentTimeMillis();
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();
                long cooldown = (target instanceof AbstractClientPlayer) ? 1000 : 500;
                if (currentTime - lastBloodSuckTime >= cooldown) {
                    // Send packet to server to request blood suck.
                    ModMessages.sendToServer(new BloodSuckC2SPacket(target.getUUID()));
                    lastBloodSuckTime = currentTime;
                }
            }
        }
        if (ToggleBuffKeybind.TOGGLE_BUFF_KEY.consumeClick()) {
            ModMessages.sendToServer(new ToggleBuffedC2SPacket());
        }
        if (FastTravelKeybind.FAST_TRAVEL_KEY.consumeClick()) {
            ModMessages.sendToServer(new FastTravelC2SPacket());
        }
        if (FeedBloodKeybind.FEED_BLOOD_KEY.consumeClick()) {
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();
                ModMessages.sendToServer(new FeedBloodC2SPacket(target.getUUID()));
            }
        }
        if (GiveUpKeybind.GIVE_UP_KEY.consumeClick()) {
            ModMessages.sendToServer(new GiveUpC2SPacket());
        }
    }
}
