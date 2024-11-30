package net.andres.cassowarymod.items;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CassowaryMod.MODID);

    public static final RegistryObject<CreativeModeTab> ANDRECHU_TAB = CREATIVE_MODE_TABS.register("andrechu_mod", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ALAMOFEMUR.get()))
            .title(Component.translatable("creativetab.andrechu_mod"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.ALAMOFEMUR.get());
                pOutput.accept(ModItems.FOSSILIZED_BONE.get());
                pOutput.accept(ModItems.FOSSIL_RADAR.get());
                pOutput.accept(ModBlocks.FEMUR_BLOCK.get());
                pOutput.accept(ModBlocks.FOSSIL_BLOCK.get());
                pOutput.accept(ModItems.DRAGONFRUIT.get());
                pOutput.accept(ModItems.PREHISTORIC_COAL.get());
                pOutput.accept(ModItems.CROCODILE_SPEAR.get());

                //Araucaria
                pOutput.accept(ModBlocks.ARAUCARIA_PLANKS.get());
                pOutput.accept(ModBlocks.ARAUCARIA_STAIRS.get());
                pOutput.accept(ModBlocks.ARAUCARIA_SLAB.get());
                pOutput.accept(ModBlocks.ARAUCARIA_BUTTON.get());
                pOutput.accept(ModBlocks.ARAUCARIA_PRESURE_PLATE.get());
                pOutput.accept(ModBlocks.ARAUCARIA_FENCE.get());
                pOutput.accept(ModBlocks.ARAUCARIA_FENCE_GATE.get());
                pOutput.accept(ModBlocks.ARAUCARIA_DOOR.get());
                pOutput.accept(ModBlocks.ARAUCARIA_TRAPDOOR.get());


            })
            .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
