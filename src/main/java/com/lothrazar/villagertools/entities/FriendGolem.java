package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.VillagerToolsRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class FriendGolem extends IronGolem {

  public FriendGolem(EntityType<? extends IronGolem> type, Level worldIn) {
    super(type, worldIn);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 100.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.ATTACK_DAMAGE, 15.0D);
  }

  @Override
  protected void registerGoals() {
    //    super.registerGoals();
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
    this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6D));
    this.goalSelector.addGoal(5, new OfferFlowerGoal(this));
    this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.of(VillagerToolsRegistry.LURE.get()), false));
    this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Player.class));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (e) -> {
      return e instanceof Enemy
          && !(e instanceof IronGolem)
          && !(e instanceof Creeper)
          && !(e instanceof GuardVindicator);
    }));
    this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
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
    return 0;
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }
}
