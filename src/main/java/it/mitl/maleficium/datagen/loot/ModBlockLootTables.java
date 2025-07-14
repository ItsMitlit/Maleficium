package it.mitl.maleficium.datagen.loot;

import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.WHITE_OAK_LOG.get());
        this.dropSelf(ModBlocks.WHITE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_WHITE_OAK_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_WHITE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.WHITE_OAK_PLANKS.get());
        this.dropSelf(ModBlocks.WHITE_OAK_SAPLING.get());

        this.add(ModBlocks.WHITE_OAK_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.WHITE_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
