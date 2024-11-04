package net.andres.cassowarymod.entity.client;

import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
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
            return new ResourceLocation("cassowary_mod_1", "geo/alamo_baby_dt.geo.json");
        }
        return new ResourceLocation("cassowary_mod_1", "geo/alamo_dt.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlamosaurusEntity alamosaurusEntity) {
        if(alamosaurusEntity.isBaby()){
            return new ResourceLocation("cassowary_mod_1","textures/entity/alamo_baby"+alamosaurusEntity.getTextureID()+".png");
        }
        return new ResourceLocation("cassowary_mod_1","textures/entity/alamo"+alamosaurusEntity.getTextureID()+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlamosaurusEntity alamosaurusEntity) {
        if(alamosaurusEntity.isBaby()){
            return new ResourceLocation("cassowary_mod_1", "animations/alamo_baby_dt.animation.json");
        }
        return new ResourceLocation("cassowary_mod_1", "animations/alamo_dt.animation.json");
    }

    //@Override
    public void setCustomAnimations(AlamosaurusEntity animatable, long instanceId, AnimationState<AlamosaurusEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");
        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
