package com.lothrazar.villagertools;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "villagertools";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain() {
    //    ConfigManager.setup();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    //    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModRegistry.ITEMS.register(eventBus);
    ModRegistry.ENTITIES.register(eventBus);
  }

  @SuppressWarnings("deprecation")
  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist  
    MinecraftForge.EVENT_BUS.register(new ItemEvents());
    SpawnPlacements.register(ModRegistry.GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
    /**
     * FORGE: Use net.minecraftforge.event.entity.EntityAttributeCreationEvent#put To be removed in 1.17 BUT!!! i coded EntityAttributeCreationEvent and it literally fails instantly
     */
  }
}
