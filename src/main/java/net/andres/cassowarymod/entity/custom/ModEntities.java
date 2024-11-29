package net.andres.cassowarymod.entity.custom;

import net.andres.cassowarymod.CassowaryMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.minecraftforge.registries.ForgeRegistries.Keys.ENTITY_TYPES;


public class ModEntities {

    public static Map<String, RegistryObject<EntityType<?>>> beastsMap;

    public static void init() {
        List<RegistryObject<EntityType<?>>> types = ENTITY_TYPES.getEntries().stream().toList();
        List<String> names = ENTITY_TYPES.getEntries().stream().map(e -> e.getId().getPath()).toList();

        beastsMap = IntStream.range(0, names.size())
                .boxed()
                .collect(Collectors.toMap(names::get, types::get));
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CassowaryMod.MODID);

    public static final RegistryObject<EntityType<CassowaryEntity>> CASSOWARY = ENTITY_TYPES.register("casuario",
            () -> EntityType.Builder.of(CassowaryEntity::new, MobCategory.CREATURE)
                    .sized(1.5f, 1.75f)
                    .build(String.valueOf(new ResourceLocation(CassowaryMod.MODID, "casuario"))));

    public static final RegistryObject<EntityType<AlamosaurusEntity>> ALAMOSAURUS = ENTITY_TYPES.register("alamosaurus",
            () -> EntityType.Builder.of(AlamosaurusEntity::new, MobCategory.CREATURE)
                    .sized(2.5f,5.75f)
                    .build(String.valueOf(new ResourceLocation(CassowaryMod.MODID, "alamosaurus"))));

}
