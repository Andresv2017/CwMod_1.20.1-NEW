package net.andres.cassowarymod.datagen;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CassowaryMod.MODID,"item/" + item.getId().getPath()));
    }
}
