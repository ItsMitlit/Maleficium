package it.mitl.maleficium.datagen;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Maleficium.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(((RotatedPillarBlock) ModBlocks.WHITE_OAK_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.WHITE_OAK_WOOD.get()), blockTexture(ModBlocks.WHITE_OAK_LOG.get()), blockTexture(ModBlocks.WHITE_OAK_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_WHITE_OAK_LOG.get(), new ResourceLocation(Maleficium.MOD_ID, "block/stripped_white_oak_log"),
                new ResourceLocation(Maleficium.MOD_ID, "block/stripped_white_oak_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_WHITE_OAK_WOOD.get(), new ResourceLocation(Maleficium.MOD_ID, "block/stripped_white_oak_log"),
                new ResourceLocation(Maleficium.MOD_ID, "block/stripped_white_oak_log"));

        blockItem(ModBlocks.WHITE_OAK_LOG);
        blockItem(ModBlocks.WHITE_OAK_WOOD);
        blockItem(ModBlocks.STRIPPED_WHITE_OAK_LOG);
        blockItem(ModBlocks.STRIPPED_WHITE_OAK_WOOD);

        blockWithItem(ModBlocks.WHITE_OAK_PLANKS);

        leavesBlock(ModBlocks.WHITE_OAK_LEAVES);
        saplingBlock(ModBlocks.WHITE_OAK_SAPLING);
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().cubeAll(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("maleficium:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath() + appendix));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("maleficium:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
