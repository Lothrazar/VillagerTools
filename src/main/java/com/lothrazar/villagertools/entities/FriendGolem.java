package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModMain;
import com.lothrazar.villagertools.ModRegistry;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.DefendVillageTargetGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PatrolVillageGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.ReturnToVillageGoal;
import net.minecraft.entity.ai.goal.ShowVillagerFlowerGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
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

  @Override
  protected void registerGoals() {
    //    super.registerGoals();
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(2, new ReturnToVillageGoal(this, 0.6D, false));
    this.goalSelector.addGoal(4, new PatrolVillageGoal(this, 0.6D));
    this.goalSelector.addGoal(5, new ShowVillagerFlowerGoal(this));
    this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
    this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.fromItems(ModRegistry.LURE.get()), false));
    this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this, PlayerEntity.class));
    //    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
      return p_234199_0_ instanceof IMob
          && !(p_234199_0_ instanceof CreeperEntity)
          && !(p_234199_0_ instanceof GuardVindicator);
    }));
    this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
  }

  @Override
  protected int getExperiencePoints(PlayerEntity player) {
    return 0;
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static class CactusGolemRenderer extends MobRenderer<FriendGolem, IronGolemModel<FriendGolem>> {

    private static final ResourceLocation TXT = new ResourceLocation(ModMain.MODID, "textures/entity/reinforced_golem.png");

    public CactusGolemRenderer(EntityRendererManager rendermanagerIn) {
      super(rendermanagerIn, new IronGolemModel(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(FriendGolem entity) {
      return TXT;
    }
    //    @Override
    //    protected void applyRotations(FriendGolem entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
    //      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    //      if (!(entityLiving.limbSwingAmount < 0.01D)) {
    //        float f = 13.0F;
    //        float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
    //        float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
    //        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
    //      }
    //    }
  }
}
