package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModMain;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class FriendGolem extends IronGolemEntity {

  public FriendGolem(EntityType<? extends IronGolemEntity> type, World worldIn) {
    super(type, worldIn);
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_()
        .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D);
  }

  public static class CactusGolemRenderer extends MobRenderer<FriendGolem, IronGolemModel<FriendGolem>> {

    private static final ResourceLocation TXT = new ResourceLocation(ModMain.MODID, "textures/entity/reinforced_golem.png");

    public CactusGolemRenderer(EntityRendererManager rendermanagerIn) {
      super(rendermanagerIn, new IronGolemModel(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(FriendGolem entity) {
      return TXT;
    }

    @Override
    protected void applyRotations(FriendGolem entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      if (!(entityLiving.limbSwingAmount < 0.01D)) {
        float f = 13.0F;
        float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
        float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
      }
    }
  }
}
