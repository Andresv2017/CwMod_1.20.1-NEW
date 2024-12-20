package net.andres.cassowarymod.event;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = CassowaryMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event){
        event.put(ModEntities.CASSOWARY.get(), CassowaryEntity.setAttributes());
        event.put(ModEntities.ALAMOSAURUS.get(), AlamosaurusEntity.setAttributes());
        event.put(ModEntities.SMILODON.get(), SmilodonEntity.setAttributes());
        event.put(ModEntities.DIPLOCAULUS.get(), DiplocaulusEntity.setAttributes());

    }
}
