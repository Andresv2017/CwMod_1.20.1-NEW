package net.andres.cassowarymod.block;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.items.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CassowaryMod.MODID);

    public static final RegistryObject<Block> FEMUR_BLOCK = registerBlock("femur_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> FOSSIL_BLOCK = registerBlock("fossil_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2f).requiresCorrectToolForDrops(), UniformInt.of(3,6))); //UniformInt Cuantos orbes de experiencia dropea//

    //Araucaria Blocks

    public static final RegistryObject<Block> ARAUCARIA_PLANKS = registerBlock("araucaria_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ARAUCARIA_STAIRS = registerBlock("araucaria_stairs",
            () -> new StairBlock(()-> ModBlocks.ARAUCARIA_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ARAUCARIA_SLAB = registerBlock("araucaria_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ARAUCARIA_BUTTON = registerBlock("araucaria_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK,10, true));

    public static final RegistryObject<Block> ARAUCARIA_PRESURE_PLATE = registerBlock("araucaria_presure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), BlockSetType.OAK));

    public static final RegistryObject<Block> ARAUCARIA_FENCE = registerBlock("araucaria_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ARAUCARIA_FENCE_GATE = registerBlock("araucaria_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));

    public static final RegistryObject<Block> ARAUCARIA_DOOR = registerBlock("araucaria_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> ARAUCARIA_TRAPDOOR = registerBlock("araucaria_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), BlockSetType.OAK));





    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T>  block ) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
