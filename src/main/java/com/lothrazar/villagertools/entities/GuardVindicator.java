package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GuardVindicator extends Vindicator {

  public GuardVindicator(EntityType<? extends Vindicator> t, Level w) {
    super(t, w);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.of(ModRegistry.LURE.get()), false));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Player.class));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (e) -> {
      return e instanceof Enemy
          && !(e instanceof IronGolem)
          && !(e instanceof Creeper)
          && !(e instanceof GuardVindicator);
    }));
  }

  @Override
  public boolean isAlliedTo(Entity entityIn) {
    if (entityIn instanceof Player) {
      return true;
    }
    if (this.getTeam() != null && this.getTeam().isAlliedTo(entityIn.getTeam())) {
      return true;
    }
    return entityIn instanceof IronGolem || entityIn instanceof GuardVindicator;
  }

  @Override
  public int getExperienceReward() {
    return 0 ;
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 100.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.ATTACK_DAMAGE, 11.0D);
  }
}
