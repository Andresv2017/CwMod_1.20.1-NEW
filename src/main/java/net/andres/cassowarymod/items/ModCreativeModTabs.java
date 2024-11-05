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
                pOutput.accept(ModBlocks.FEMUR_BLOCK.get());
            })
            .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
