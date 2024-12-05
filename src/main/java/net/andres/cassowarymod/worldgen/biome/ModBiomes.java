package net.andres.cassowarymod.worldgen.biome;

import net.andres.cassowarymod.CassowaryMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;


public class ModBiomes {
    public static final ResourceKey<Biome> TEST_BIOME = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(CassowaryMod.MODID, "test_biome"));

    public static void boostrap(BootstapContext<Biome> context) {
        context.register(TEST_BIOME, testBiome(context));
    }

    public static Biome testBiome(BootstapContext<Biome> context) {
        // Configuración de spawns de mobs
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.oceanSpawns(spawnBuilder, 10, 2, 4); // Spawns predeterminados de océano
        BiomeDefaultFeatures.warmOceanSpawns(spawnBuilder, 2, 4); // Spawns de océanos cálidos

        // Configuración de generación del bioma usando baseOceanGeneration
        BiomeGenerationSettings.Builder generationBuilder = baseOceanGeneration(
                context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER)
        );

        // Personaliza la generación si es necesario
        generationBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                AquaticPlacements.SEAGRASS_NORMAL
        );
        BiomeDefaultFeatures.addFossilDecoration(generationBuilder);


        // Crea el bioma usando baseOcean
        return baseOcean(
                spawnBuilder,
                0x3d57d6, // Color del agua (azul oscuro)
                0x050533, // Color de la niebla en el agua (oscura)
                generationBuilder
        );
    }

    private static Biome baseOcean(MobSpawnSettings.Builder pMobSpawnSettings, int pWaterColor, int pWaterFogColor, BiomeGenerationSettings.Builder pGenerationSettings) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(pWaterColor)
                        .waterFogColor(pWaterFogColor)
                        .grassColorOverride(0x7f03fc)
                        .fogColor(0xc0d8ff) // Color de niebla general
                        .skyColor(0x77adff) // Color del cielo
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(pMobSpawnSettings.build())
                .generationSettings(pGenerationSettings.build())
                .build();
    }

    private static BiomeGenerationSettings.Builder baseOceanGeneration(HolderGetter<PlacedFeature> pPlacedFeatures, HolderGetter<ConfiguredWorldCarver<?>> pWorldCarvers) {
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(pPlacedFeatures, pWorldCarvers);
        BiomeDefaultFeatures.addDefaultOres(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultSeagrass(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenerationSettings);
        return biomeGenerationSettings;
    }


}

