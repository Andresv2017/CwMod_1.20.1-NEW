package net.andres.cassowarymod.datagen;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CassowaryMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.FOSSIL_BLOCK);
        blockWithItem(ModBlocks.FEMUR_BLOCK);

        //Araucaria Blocks
        blockWithItem(ModBlocks.ARAUCARIA_PLANKS);
        stairsBlock(((StairBlock) ModBlocks.ARAUCARIA_STAIRS.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        slabBlock(((SlabBlock) ModBlocks.ARAUCARIA_SLAB.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        buttonBlock(((ButtonBlock) ModBlocks.ARAUCARIA_BUTTON.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.ARAUCARIA_PRESURE_PLATE.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        fenceBlock(((FenceBlock) ModBlocks.ARAUCARIA_FENCE.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.ARAUCARIA_FENCE_GATE.get()), blockTexture(ModBlocks.ARAUCARIA_PLANKS.get()));
        doorBlockWithRenderType(((DoorBlock) ModBlocks.ARAUCARIA_DOOR.get()), modLoc("block/araucaria_door_bottom"), modLoc("block/araucaria_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.ARAUCARIA_TRAPDOOR.get()), modLoc("block/araucaria_trapdoor"), true, "cutout");

    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
