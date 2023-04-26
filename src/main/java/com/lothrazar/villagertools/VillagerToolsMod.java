package com.lothrazar.villagertools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VillagerToolsMod.MODID)
public class VillagerToolsMod {

  public static final String MODID = "villagertools";
  public static final Logger LOGGER = LogManager.getLogger();

  public VillagerToolsMod() {
    //    ConfigManager.setup();
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    VillagerToolsRegistry.ITEMS.register(bus);
    VillagerToolsRegistry.ENTITIES.register(bus);
    bus.addListener(this::setup);
  }

  private void setup(final FMLCommonSetupEvent event) {
    new ItemEvents();
  }
}
