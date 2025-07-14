package it.mitl.maleficium.datagen;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                               CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, Maleficium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.WHITE_OAK_LOG.get().asItem())
                .add(ModBlocks.WHITE_OAK_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_WHITE_OAK_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_WHITE_OAK_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.WHITE_OAK_PLANKS.get().asItem());
    }

    @Override
    public String getName() {
        return "Item Tags";
    }
}
