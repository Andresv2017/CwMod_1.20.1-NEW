package net.andres.cassowarymod.datagen;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.block.ModBlocks;
import net.andres.cassowarymod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CassowaryMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.ALAMOFEMUR);
        simpleItem(ModItems.DRAGONFRUIT);
        simpleItem(ModItems.FOSSIL_RADAR);
        simpleItem(ModItems.FOSSILIZED_BONE);
        simpleItem(ModItems.PREHISTORIC_COAL);

        simpleBlockItem(ModBlocks.ARAUCARIA_DOOR);
        fenceItem(ModBlocks.ARAUCARIA_FENCE, ModBlocks.ARAUCARIA_PLANKS);
        buttonItem(ModBlocks.ARAUCARIA_BUTTON, ModBlocks.ARAUCARIA_PLANKS);

        evenSimplerBlockItem(ModBlocks.ARAUCARIA_STAIRS);
        evenSimplerBlockItem(ModBlocks.ARAUCARIA_SLAB);
        evenSimplerBlockItem(ModBlocks.ARAUCARIA_PRESURE_PLATE);
        evenSimplerBlockItem(ModBlocks.ARAUCARIA_FENCE_GATE);

        trapdoorItem(ModBlocks.ARAUCARIA_TRAPDOOR);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CassowaryMod.MODID,"item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(CassowaryMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(CassowaryMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(CassowaryMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    //Door
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CassowaryMod.MODID,"item/" + item.getId().getPath()));
    }
}
