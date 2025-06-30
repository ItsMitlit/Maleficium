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
                .addCriterion("on_join",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "maleficium"), existingFileHelper);

        // Vampires
        Advancement becomeVampireAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.BAT_SPAWN_EGG),
                        Component.literal("Become a Vampire"), Component.literal("Become a creature of the night"),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(rootAdvancement)
                .addCriterion("become_vampire",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "become_vampire"), existingFileHelper);
        Advancement firstBloodAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.COOKED_CHICKEN),
                        Component.literal("First Blood"), Component.literal("Your first taste of blood... Tastes like chicken!"),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(becomeVampireAdvancement)
                .addCriterion("first_blood",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "first_blood"), existingFileHelper);
        Advancement aFulfillingMealAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.COOKED_BEEF),
                        Component.literal("A Fulfilling Meal"), Component.literal("Drain a mob to death."),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(firstBloodAdvancement)
                .addCriterion("a_fulfilling_meal",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "a_fulfilling_meal"), existingFileHelper);
        Advancement enemyOfTheVillageAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.VILLAGER_SPAWN_EGG),
                        Component.literal("Enemy of the Village"), Component.literal("Kill a villager by draining them."),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(firstBloodAdvancement)
                .addCriterion("enemy_of_the_village",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "enemy_of_the_village"), existingFileHelper);

        // Witches
        Advancement becomeWitchAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.BREWING_STAND),
                        Component.literal("Become a Witch"), Component.literal("Become a master of magic"),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(rootAdvancement)
                .addCriterion("become_witch",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "become_witch"), existingFileHelper);

        // Werewolves
        Advancement becomeWerewolfAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(Items.BONE),
                        Component.literal("Become a Werewolf"), Component.literal("Become a beast of the full moon"),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(rootAdvancement)
                .addCriterion("become_werewolf",
                        new ImpossibleTrigger.TriggerInstance())
                .save(saver, new ResourceLocation(Maleficium.MOD_ID, "become_werewolf"), existingFileHelper);
    }
}
