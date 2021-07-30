package com.lothrazar.villagertools.entities;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class HurtByNotPlayersGoal extends HurtByTargetGoal {

  public HurtByNotPlayersGoal(PathfinderMob cr, Class<?>[] clz) {
    super(cr, clz);
  }
}
