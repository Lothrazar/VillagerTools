package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.VillagerToolsMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.golem.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.resources.Identifier;

public class FriendGolemRenderer extends MobRenderer<FriendGolem, IronGolemRenderState, IronGolemModel> {

  private static final Identifier TXT = Identifier.fromNamespaceAndPath(VillagerToolsMod.MODID, "textures/entity/reinforced_golem.png");

  public FriendGolemRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new IronGolemModel(ctx.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
  }

  @Override
  public Identifier getTextureLocation(IronGolemRenderState state) {
    return TXT;
  }

  @Override
  public IronGolemRenderState createRenderState() {
    return new IronGolemRenderState();
  }

  @Override
  protected void setupRotations(IronGolemRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
    super.setupRotations(state, poseStack, bodyRot, entityScale);
    if (!(state.walkAnimationSpeed < 0.01D)) {
      float wp = state.walkAnimationPos + 6.0F;
      float triangleWave = (Math.abs(wp % 13.0F - 6.5F) - 3.25F) / 3.25F;
      poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * triangleWave));
    }
  }
}
