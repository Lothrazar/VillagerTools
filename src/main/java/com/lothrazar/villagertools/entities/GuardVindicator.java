package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModMain;
import com.lothrazar.villagertools.ModRegistry;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.VindicatorRenderer;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuardVindicator extends VindicatorEntity {

  public GuardVindicator(EntityType<? extends VindicatorEntity> t, World w) {
    super(t, w);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new SwimGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.fromItems(ModRegistry.LURE.get()), false));
    this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, PlayerEntity.class)).setCallsForHelp(GuardVindicator.class));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (e) -> {
      return !(e instanceof IronGolemEntity)
          && !(e instanceof CreeperEntity)
          && !(e instanceof FriendGolem)
          && !(e instanceof GuardVindicator);
    }));
  }

  @Override
  public boolean isOnSameTeam(Entity entityIn) {
    return this.isOnScoreboardTeam(entityIn.getTeam()) || entityIn instanceof PlayerEntity;
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
