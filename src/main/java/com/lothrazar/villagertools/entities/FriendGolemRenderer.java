package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModMain;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FriendGolemRenderer extends MobRenderer<FriendGolem, IronGolemModel<FriendGolem>> {

  private static final ResourceLocation TXT = new ResourceLocation(ModMain.MODID, "textures/entity/reinforced_golem.png");

  public FriendGolemRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new IronGolemModel<>(ctx.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
  }

  @Override
  public ResourceLocation getTextureLocation(FriendGolem entity) {
    return TXT;
  }

  protected void setupRotations(FriendGolem g, PoseStack ps, float a, float b, float c) {
    super.setupRotations(g, ps, a, b, c);
    if (!((double) g.animationSpeed < 0.01D)) {
//      float f = 13.0F;
      float f1 = g.animationPosition - g.animationSpeed * (1.0F - c) + 6.0F;
      float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
      ps.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
    }
  }
}
