package net.andres.cassowarymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AlamosaurusRenderer extends GeoEntityRenderer<AlamosaurusEntity> {
    public AlamosaurusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlamosaurusModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AlamosaurusEntity animatable) {
        if(animatable.isBaby()){
            return new ResourceLocation("cassowarymod", "textures/entity/alamo_baby1_dt.png");
        }
        return new ResourceLocation("cassowarymod", "textures/entity/alamo1_dt.png");
    }

    @Override
    public void render(AlamosaurusEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight){
        if(entity.isBaby()){
            poseStack.scale(1F,1F,1F);
        }else {
            poseStack.scale(2F,2F,2F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
