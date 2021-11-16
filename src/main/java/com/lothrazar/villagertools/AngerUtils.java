package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.GuardVindicator;
import net.minecraft.entity.passive.IronGolemEntity;

public class AngerUtils {

  // https://github.com/Lothrazar/AngerManagement/blob/trunk/1.16/src/main/java/com/lothrazar/angermanagement/util/AngerUtils.java
  public static void makeCalmGolem(IronGolemEntity golem) {
    golem.setAttackTarget(null);
    golem.setRevengeTarget(null);
    golem.setLastAttackedEntity(null);
    golem.setAngerTarget(null);
    golem.setAngerTime(0);
  }

  public static void makeCalm(GuardVindicator golem) {
    golem.setAttackTarget(null);
    golem.setRevengeTarget(null);
    golem.setLastAttackedEntity(null);
  }
}
