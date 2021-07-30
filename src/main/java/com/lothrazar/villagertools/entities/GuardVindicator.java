package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.ModRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GuardVindicator extends Vindicator {

  public GuardVindicator(EntityType<? extends Vindicator> t, Level w) {
    super(t, w);
  }

  @Override
  protected void registerGoals() {
    //    super.registerGoals(); 
    this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    //    this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, () -> false));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new TemptGoal(this, 0.666, Ingredient.of(ModRegistry.LURE.get()), false));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
    this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6D));
    this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    //    this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
    this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Player.class)).setAlertOthers(GuardVindicator.class));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Vindicator.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Ravager.class, true));
  }

  @Override
  protected int getExperienceReward(Player player) {
    return 0;
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
