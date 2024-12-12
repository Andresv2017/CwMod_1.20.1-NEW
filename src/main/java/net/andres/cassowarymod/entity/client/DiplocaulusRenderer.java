package net.andres.cassowarymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.andres.cassowarymod.entity.custom.AlamosaurusEntity;
import net.andres.cassowarymod.entity.custom.DiplocaulusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DiplocaulusRenderer extends GeoEntityRenderer<DiplocaulusEntity> {
    public DiplocaulusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DiplocaulusModel());
    }


    @Override
    public ResourceLocation getTextureLocation(DiplocaulusEntity animatable) {
        if(animatable.isBaby()){
            return new ResourceLocation("cassowary_mod_1","textures/entity/diplocaulus_baby0.png");
        }
        return new ResourceLocation("cassowary_mod_1","textures/entity/diplocaulus"+animatable.getTextureID()+".png");
    }


    @Override
    public void render(DiplocaulusEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight){
        if(entity.isBaby()){
            poseStack.scale(1F,1F,1F);
        }else {
            poseStack.scale(1F,1F,1F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
