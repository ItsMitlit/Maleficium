package it.mitl.maleficium.datagen;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Maleficium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.WHITE_OAK_LOG.get())
                .add(ModBlocks.WHITE_OAK_WOOD.get())
                .add(ModBlocks.STRIPPED_WHITE_OAK_LOG.get())
                .add(ModBlocks.STRIPPED_WHITE_OAK_WOOD.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.WHITE_OAK_PLANKS.get());
    }

    @Override
    public String getName() {
        return "Block Tags";
    }
}
