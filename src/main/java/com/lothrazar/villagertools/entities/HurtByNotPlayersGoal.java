package com.lothrazar.villagertools.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

public class HurtByNotPlayersGoal extends HurtByTargetGoal {

  public HurtByNotPlayersGoal(CreatureEntity cr, Class<?>[] clz) {
    super(cr, clz);
  }
}
