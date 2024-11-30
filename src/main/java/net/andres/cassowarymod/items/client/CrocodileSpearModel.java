package net.andres.cassowarymod.items.client;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.items.custom.CrocodileSpear;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CrocodileSpearModel extends GeoModel<CrocodileSpear> {

    @Override
    public ResourceLocation getModelResource(CrocodileSpear animatable) {
        return new ResourceLocation(CassowaryMod.MODID, "geo/crocodile_spear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CrocodileSpear animatable) {
        return new ResourceLocation(CassowaryMod.MODID, "textures/item/crocodile_spear.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CrocodileSpear animatable) {
        return null;
    }
}

