package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.GuardVindicator;
import net.minecraft.world.entity.animal.IronGolem;

public class AngerUtils {

  // https://github.com/Lothrazar/AngerManagement/blob/trunk/1.16/src/main/java/com/lothrazar/angermanagement/util/AngerUtils.java
  public static void makeCalmGolem(IronGolem golem) {
    golem.setRemainingPersistentAngerTime(0);
    golem.setTarget(null);

//    golem.setAttackTarget(null);
//    golem.setRevengeTarget(null);
//    golem.setLastAttackedEntity(null);
//    golem.setAngerTarget(null);
//    golem.setAngerTime(0);
  }

  public static void makeCalm(GuardVindicator golem) {
    golem.setTarget(null);
//    golem.setAttackTarget(null);
//    golem.setRevengeTarget(null);
//    golem.setLastAttackedEntity(null);
  }
}
