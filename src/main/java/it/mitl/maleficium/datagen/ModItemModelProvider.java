package it.mitl.maleficium.datagen;

import it.mitl.maleficium.Maleficium;
import it.mitl.maleficium.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Maleficium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        saplingItem(ModBlocks.WHITE_OAK_SAPLING);
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Maleficium.MOD_ID,"block/" + item.getId().getPath()));
    }
}
