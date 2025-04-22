package it.mitl.mclegacies.client;

import com.mojang.blaze3d.systems.RenderSystem;
import it.mitl.mclegacies.MCLegacies;
import it.mitl.mclegacies.subroutine.VariableManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = MCLegacies.MOD_ID, value = Dist.CLIENT)
public class ExperienceBarColourChanger {

    @SubscribeEvent
    public static void onRenderExperienceBar(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player == null) return;
            if (player.isCreative() || player.isSpectator()) return;
            if (player.getVehicle() != null) return;

            if ("human".equals(VariableManager.getSpecies(player))) return;

            XPColor color = getXPColorForSpecies(Objects.requireNonNull(VariableManager.getSpecies(player)));

            event.setCanceled(true);

            GuiGraphics graphics = event.getGuiGraphics();

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int centerX = screenWidth / 2;

            int barWidth = 182;
            int x = centerX - barWidth / 2;
            int y = screenHeight - 32 + 3;

            int experienceBarLength = (int)(player.experienceProgress * (float)(barWidth + 1));

            ResourceLocation guiIcons = new ResourceLocation("textures/gui/icons.png"); // minecraft default gui icon png
            graphics.blit(guiIcons, x, y, 0, 64, barWidth, 5); // background

            graphics.pose().pushPose();

            RenderSystem.setShaderColor(color.r, color.g, color.b, color.a);

            // Draw the filled part
            graphics.blit(guiIcons, x, y, 0, 69, experienceBarLength, 5);

            // Reset color
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            graphics.pose().popPose();

            // Draw the level number
            if (player.experienceLevel > 0) {
                String levelString = String.valueOf(player.experienceLevel);
                int levelX = centerX - mc.font.width(levelString) / 2;
                int levelY = y - 9;
                graphics.drawString(mc.font, levelString, levelX, levelY, color.textColor, true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHungerBar(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().toString().equals("minecraft:food_level")) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player == null) return;
            if (player.isCreative() || player.isSpectator()) return;
            if (player.getVehicle() != null) return;

            // Ignore if the player is not a vampire
            if (!"vampire".equals(VariableManager.getSpecies(player))) return;

            event.setCanceled(true);

            // Draw custom hunger bar
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int foodLevel = player.getFoodData().getFoodLevel(); // 0–20 (bc minecraft)
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int xStart = screenWidth / 2 + 83; // 83 pixels to the right of center
            int yStart = screenHeight - 39;    // slightly above bottom
            ResourceLocation HUNGER_ICONS = new ResourceLocation("mclegacies", "textures/gui/blood_drops.png");

            for (int i = 0; i < 10; i++) {
                int x = xStart - i * 8; // icons go left from the starting point
                int y = yStart;

                int u;
                if (foodLevel >= (i + 1) * 2) {
                    u = 0; // full
                } else if (foodLevel == (i * 2) + 1) {
                    u = 14; // half
                } else {
                    u = 7; // empty
                }

                guiGraphics.blit(HUNGER_ICONS, x, y, u, 0, 7, 9, 21, 9);
            }
        }
    }




    private static class XPColor {
        float r, g, b, a;
        int textColor;
        XPColor(float r, float g, float b, float a, int textColor) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.textColor = textColor;
        }
    }

    private static XPColor getXPColorForSpecies(String species) {
        return switch (species) {
            case "vampire" -> new XPColor(165/255f, 7/255f, 7/255f, 1.0f, 0xA50707);
            case "werewolf" -> new XPColor(67/255f, 44/255f, 8/255f, 1.0f, 0x432C08);
            case "witch" -> new XPColor(152/255f, 95/255f, 186/255f, 1.0f, 0x985FBA);
            default -> new XPColor(0f, 0f, 0f, 1.0f,0xFFFFFF);
        };
    }
}
