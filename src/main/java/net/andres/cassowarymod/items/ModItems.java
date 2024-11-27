package net.andres.cassowarymod.items;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.items.custom.FossilRadarItem;
import net.andres.cassowarymod.items.custom.FuelItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CassowaryMod.MODID);


    public static final RegistryObject<Item> ALAMOFEMUR = ITEMS.register("alamofemur",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FOSSILIZED_BONE = ITEMS.register("fossilized_bone",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FOSSIL_RADAR = ITEMS.register("fossil_radar",
            () -> new FossilRadarItem(new Item.Properties().durability(4))); // Espeficiar que nuevo obejto estoy creando

    public static final RegistryObject<Item> DRAGONFRUIT = ITEMS.register("dragon_fruit",
            () -> new Item(new Item.Properties().food(ModFoods.DRAGONFRUIT)));

    public static final RegistryObject<Item> PREHISTORIC_COAL = ITEMS.register("prehistoric_coal",
            () -> new FuelItem(new Item.Properties(), 400));

    public  static  void  register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
