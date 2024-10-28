package net.andres.cassowarymod.entity.client;

import net.andres.cassowarymod.CassowaryMod;
import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.andres.cassowarymod.entity.custom.CassowaryEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AlamosaurusModel extends GeoModel<AlamosaurusEntity> {

    @Override
    public ResourceLocation getModelResource(AlamosaurusEntity alamosaurusEntity) {
        if(alamosaurusEntity.isBaby()){
            return new ResourceLocation("cassowarymod", "geo/alamo_baby_dt.json");
        }
        return new ResourceLocation("cassowarymod", "geo/alamosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlamosaurusEntity alamosaurusEntity) {
        if(alamosaurusEntity.isBaby()){
            return new ResourceLocation("cassowarymod", "textures/entity/alamo_baby1_dt.png");
        }
        return new ResourceLocation("cassowarymod", "textures/entity/alamo1_dt.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlamosaurusEntity alamosaurusEntity) {
        if(alamosaurusEntity.isBaby()){
            return new ResourceLocation("cassowarymod", "animations/alamo_baby_dt.animation.json");
        }
        return new ResourceLocation("cassowarymod", "animations/alamosaurus.animation.json");
    }

    //@Override
    public void setCustomAnimations(CassowaryEntity animatable, long instanceId, AnimationState<CassowaryEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");
        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
