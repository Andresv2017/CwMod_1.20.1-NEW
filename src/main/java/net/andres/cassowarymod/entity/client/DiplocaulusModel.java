package net.andres.cassowarymod.entity.client;

import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.andres.cassowarymod.entity.custom.DiplocaulusEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DiplocaulusModel extends GeoModel<DiplocaulusEntity> {

    @Override
    public ResourceLocation getModelResource(DiplocaulusEntity diplocaulusEntity) {
        if(diplocaulusEntity.isBaby()){
            return new ResourceLocation("cassowary_mod_1", "geo/diplocaulus_baby.geo.json");
        }
        return new ResourceLocation("cassowary_mod_1", "geo/diplocaulus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DiplocaulusEntity diplocaulusEntity) {
        if(diplocaulusEntity.isBaby()){
            return new ResourceLocation("cassowary_mod_1","textures/entity/diplocaulus_baby0.png");
        }
        return new ResourceLocation("cassowary_mod_1","textures/entity/diplocaulus"+diplocaulusEntity.getTextureID()+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(DiplocaulusEntity diplocaulusEntity) {
        if(diplocaulusEntity.isBaby()){
            return new ResourceLocation("cassowary_mod_1", "animations/diplocaulus_baby.animation.json");
        }
        return new ResourceLocation("cassowary_mod_1", "animations/diplocaulus.animation.json");
    }

    @Override
    public void setCustomAnimations(DiplocaulusEntity animatable, long instanceId, AnimationState<DiplocaulusEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
