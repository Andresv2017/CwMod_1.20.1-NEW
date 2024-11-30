package net.andres.cassowarymod.datagen.loot;

import net.andres.cassowarymod.block.ModBlocks;
import net.andres.cassowarymod.items.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.FEMUR_BLOCK.get());

        this.add(ModBlocks.FOSSIL_BLOCK.get(),
                block -> createFossilDrops(ModBlocks.FOSSIL_BLOCK.get(), ModItems.FOSSILIZED_BONE.get()));

        //Dropeos Araucaria
        this.dropSelf(ModBlocks.ARAUCARIA_PLANKS.get());
        this.dropSelf(ModBlocks.ARAUCARIA_STAIRS.get());
        this.dropSelf(ModBlocks.ARAUCARIA_BUTTON.get());
        this.dropSelf(ModBlocks.ARAUCARIA_PRESURE_PLATE.get());
        this.dropSelf(ModBlocks.ARAUCARIA_TRAPDOOR.get());
        this.dropSelf(ModBlocks.ARAUCARIA_FENCE.get());
        this.dropSelf(ModBlocks.ARAUCARIA_FENCE_GATE.get());

        this.add(ModBlocks.ARAUCARIA_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.ARAUCARIA_SLAB.get()));
        this.add(ModBlocks.ARAUCARIA_DOOR.get(),
            block -> createDoorTable(ModBlocks.ARAUCARIA_DOOR.get()));
    }

    protected LootTable.Builder createFossilDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
