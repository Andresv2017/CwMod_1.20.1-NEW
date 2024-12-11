package net.andres.cassowarymod.entity.client;

import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.andres.cassowarymod.entity.custom.SmilodonEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SmilodonModel extends GeoModel<SmilodonEntity> {

    @Override
    public ResourceLocation getModelResource(SmilodonEntity animatable) {
        return new ResourceLocation("cassowary_mod_1","geo/smilodon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmilodonEntity animatable) {
        return new ResourceLocation("cassowary_mod_1","textures/entity/smilodon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmilodonEntity animatable) {
        return new ResourceLocation("cassowary_mod_1","animations/smilodon.animation.json");
    }

    @Override
    public void setCustomAnimations(SmilodonEntity animatable, long instanceId, AnimationState<SmilodonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
