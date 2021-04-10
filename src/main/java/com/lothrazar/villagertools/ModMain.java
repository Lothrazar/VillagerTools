package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.FriendGolem.CactusGolemRenderer;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModRegistry.ITEMS.register(eventBus);
    ModRegistry.ENTITIES.register(eventBus);
  }

  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist  
    MinecraftForge.EVENT_BUS.register(new ItemEvents());
    EntitySpawnPlacementRegistry.register(ModRegistry.GOLEM.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
    /** FORGE: Use net.minecraftforge.event.entity.EntityAttributeCreationEvent#put To be removed in 1.17 */
    GlobalEntityTypeAttributes.put(ModRegistry.GOLEM.get(), FriendGolem.createAttributes().create());
  }

  private void setupClient(final FMLClientSetupEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(ModRegistry.GOLEM.get(), CactusGolemRenderer::new);
  }
}
