package net.andres.cassowarymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.andres.cassowarymod.entity.custom.CassowaryEntity;
import net.andres.cassowarymod.entity.custom.SmilodonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmilodonRenderer extends GeoEntityRenderer<SmilodonEntity> {

    public SmilodonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SmilodonModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SmilodonEntity animatable) {
        return new ResourceLocation("cassowary_mod_1", "textures/entity/smilodon.png");
    }

    @Override
    public void render(SmilodonEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight){
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
