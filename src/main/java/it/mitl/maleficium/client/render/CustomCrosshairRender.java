package it.mitl.maleficium.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.subroutine.VariableManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Maleficium.MOD_ID, value = Dist.CLIENT)
public class CustomCrosshairRender {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation VAMPIRE_CROSSHAIR = new ResourceLocation(Maleficium.MOD_ID, "textures/gui/vampire_crosshair.png");

    @SubscribeEvent
    public static void onRenderVampireCrosshair(RenderGuiOverlayEvent.Pre event) {
        if (!event.getOverlay().id().toString().equals("minecraft:crosshair")) return;
        if (mc.player == null || mc.level == null) return;
        if (!"vampire".equals(VariableManager.getSpecies(mc.player))) return; // Only render for vampires

        HitResult hit = mc.crosshairPickEntity != null ? new EntityHitResult(mc.crosshairPickEntity) : mc.hitResult;

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingEntity) {
            if ((livingEntity.getMobType() != MobType.UNDEAD && livingEntity.getMaxHealth() <= 40.0f) || livingEntity instanceof Player) {
                event.setCanceled(true);

                GuiGraphics graphics = event.getGuiGraphics();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, VAMPIRE_CROSSHAIR);

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                int crosshairSize = 16;

                graphics.blit(VAMPIRE_CROSSHAIR,
                        (screenWidth - crosshairSize) / 2,
                        (screenHeight - crosshairSize) / 2,
                        0, 0,
                        crosshairSize, crosshairSize,
                        crosshairSize, crosshairSize);
            }
        }
    }
}
