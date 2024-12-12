package net.andres.cassowarymod;

import com.mojang.logging.LogUtils;
import net.andres.cassowarymod.block.ModBlocks;
import net.andres.cassowarymod.entity.client.AlamosaurusRenderer;
import net.andres.cassowarymod.entity.client.CassowaryRenderer;
import net.andres.cassowarymod.entity.client.DiplocaulusRenderer;
import net.andres.cassowarymod.entity.client.SmilodonRenderer;
import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.andres.cassowarymod.entity.custom.CassowaryEntity;
import net.andres.cassowarymod.entity.custom.ModEntities;
import net.andres.cassowarymod.items.ModCreativeModTabs;
import net.andres.cassowarymod.items.ModItems;
import net.andres.cassowarymod.worldgen.biome.surface.ModSurfaceRules;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CassowaryMod.MODID)
public class CassowaryMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cassowary_mod_1";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public CassowaryMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEntities.init();

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
                    SpawnPlacements.register(ModEntities.ALAMOSAURUS.get(), SpawnPlacements.Type.ON_GROUND,
                            Heightmap.Types.MOTION_BLOCKING, AlamosaurusEntity::checkAlamoSpawnRules);

                    SpawnPlacements.register(ModEntities.CASSOWARY.get(), SpawnPlacements.Type.ON_GROUND,
                            Heightmap.Types.MOTION_BLOCKING, CassowaryEntity::checkCassoSpawnRules);


            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRules.makeRules());
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
           event.accept(ModItems.ALAMOFEMUR);
           event.accept(ModItems.FOSSILIZED_BONE);
           event.accept(ModItems.FOSSIL_RADAR);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.CASSOWARY.get(), CassowaryRenderer::new);
            EntityRenderers.register(ModEntities.ALAMOSAURUS.get(), AlamosaurusRenderer::new);
            EntityRenderers.register(ModEntities.DIPLOCAULUS.get(), DiplocaulusRenderer::new);
            EntityRenderers.register(ModEntities.SMILODON.get(), SmilodonRenderer::new);

        }
    }
}
