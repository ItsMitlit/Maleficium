package it.mitl.maleficium.datagen;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement rootAdvancement = Advancement.Builder.advancement()
                .display(
                        new DisplayInfo(new ItemStack(ModItems.WHITE_OAK_STAKE.get()),
                        Component.literal("Maleficium"), Component.literal("Magic in Minecraft"),
                        new ResourceLocation("minecraft", "textures/particle/enchanted_hit.png"), FrameType.TASK,
                        true, true, false))
                .addCriterion("on_join", new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "maleficium"), existingFileHelper);
    }
}
