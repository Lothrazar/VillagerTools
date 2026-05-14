package com.lothrazar.villagertools;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigManager {

  private static final ModConfigSpec.Builder CFG = new ModConfigSpec.Builder();
  static ModConfigSpec CONFIG;


  private static void initConfig() {
    CFG.comment("General settings").push(VillagerToolsMod.MODID);
    //    TESTING = CFG.comment("Testing mixin spam log if holding filled map").define("serverTest", true);
    CFG.pop(); // one pop for every push
    CONFIG = CFG.build();
  }

}
