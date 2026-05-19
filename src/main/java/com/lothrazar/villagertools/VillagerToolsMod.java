package com.lothrazar.villagertools;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(VillagerToolsMod.MODID)
public class VillagerToolsMod {

  public static final String MODID = "villagertools";
  public static final Logger LOGGER = LogManager.getLogger();

  public VillagerToolsMod(IEventBus bus, ModContainer modContainer) {
//    modContainer.registerConfig(ModConfig.Type.COMMON, ConfigManager.CONFIG);
//TODO: add value to config and then enable
    VillagerToolsRegistry.ITEMS.register(bus);
    VillagerToolsRegistry.ENTITIES.register(bus);
    bus.addListener(this::setup);
  }

  private void setup(final FMLCommonSetupEvent event) {
    new ItemEvents();
  }
}
