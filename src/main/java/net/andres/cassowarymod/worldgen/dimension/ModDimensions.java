package net.andres.cassowarymod.worldgen.dimension;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;


import java.util.List;
import java.util.OptionalLong;


public class ModDimensions {

    public static final ResourceKey<LevelStem> TEST_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(CassowaryMod.MODID, "testdim"));
    public static final ResourceKey<Level> TEST_DIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(CassowaryMod.MODID, "testdim"));
    public static final ResourceKey<DimensionType> TEST_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(CassowaryMod.MODID, "test_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(TEST_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                com.mojang.datafixers.util.Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.7F, -0.1F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.SNOWY_PLAINS)),
                                com.mojang.datafixers.util.Pair.of(
                                        Climate.parameters(0.26F, 0.0F, 0.7F, 0.0F, 0.0F, 0.0F, 0.0F),
                                        biomeRegistry.getOrThrow(Biomes.SPARSE_JUNGLE)),
                                com.mojang.datafixers.util.Pair.of(
                                        Climate.parameters(0.5F, 0.2F, -1F, -0.5F, 0.0F, 0.0F, 0.0F)
                                        , biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME))
                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));


        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.TEST_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(TEST_KEY, stem);
    }
}
