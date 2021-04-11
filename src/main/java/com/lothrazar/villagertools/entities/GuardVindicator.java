package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModMain;
import com.lothrazar.villagertools.ModRegistry;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.VindicatorRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PatrolVillageGoal;
import net.minecraft.entity.ai.goal.ReturnToVillageGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuardVindicator extends VindicatorEntity {

  public GuardVindicator(EntityType<? extends VindicatorEntity> t, World w) {
    super(t, w);
    PatrollerEntity xyz;
  }

  @Override
  protected void registerGoals() {
    //    super.registerGoals(); 
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    //    this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, () -> false));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.fromItems(ModRegistry.LURE.get()), false));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(2, new ReturnToVillageGoal(this, 0.6D, false));
    this.goalSelector.addGoal(4, new PatrolVillageGoal(this, 0.6D));
    this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
    this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, PlayerEntity.class)).setCallsForHelp(GuardVindicator.class));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, SpiderEntity.class, true));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, false));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, VindicatorEntity.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PillagerEntity.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, RavagerEntity.class, true));
  }

  @Override
  protected int getExperiencePoints(PlayerEntity player) {
    return 0;
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    SkeletonEntity y;
    return MobEntity.func_233666_p_()
        .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 11.0D);
  }

  @SuppressWarnings("hiding")
  public static class GuardRender<GuardVindicator> extends VindicatorRenderer {

    private static final ResourceLocation TXT = new ResourceLocation(ModMain.MODID, "textures/entity/guard.png");

    public GuardRender(EntityRendererManager rendermanagerIn) {
      super(rendermanagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(VindicatorEntity entity) {
      return TXT;
    }
  }
}
