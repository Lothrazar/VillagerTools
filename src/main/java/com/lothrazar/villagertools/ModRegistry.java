package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.FriendGolemRenderer;
import com.lothrazar.villagertools.entities.GuardRender;
import com.lothrazar.villagertools.entities.GuardVindicator;
import com.lothrazar.villagertools.item.ItemVillager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

  public static final CreativeModeTab TAB = new CreativeModeTab(ModMain.MODID) {

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack makeIcon() {
      return new ItemStack(LURE.get());
    }
  };
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModMain.MODID);
  //
  public static final RegistryObject<Item> LURE = ITEMS.register("lure", () -> new ItemVillager(new Item.Properties().stacksTo(1).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> GEARS = ITEMS.register("gears", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> BRIBE = ITEMS.register("bribe", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> RESTOCK = ITEMS.register("restock", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> FORGET = ITEMS.register("forget", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> CONTRACT = ITEMS.register("contract", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> DARKNESS = ITEMS.register("darkness", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> GUARD_ITEM = ITEMS.register("guard", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> KNOWLEDGE = ITEMS.register("knowledge", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> KEY = ITEMS.register("key", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> BADGE = ITEMS.register("badge", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  public static final RegistryObject<Item> CURE = ITEMS.register("cure", () -> new ItemVillager(new Item.Properties().stacksTo(64).tab(ModRegistry.TAB)));
  //
  public static final RegistryObject<EntityType<GuardVindicator>> GUARDENTITY = ENTITIES.register("guard", () -> register("guard",
      EntityType.Builder.<GuardVindicator>of(GuardVindicator::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));
  public static final RegistryObject<EntityType<FriendGolem>> GOLEM = ENTITIES.register("reinforced_golem", () -> register("reinforced_golem",
      EntityType.Builder.<FriendGolem>of(FriendGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));

  public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(id);
  }

  @SubscribeEvent
  public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
    event.put(ModRegistry.GOLEM.get(), FriendGolem.createAttributes().build());
    event.put(ModRegistry.GUARDENTITY.get(), GuardVindicator.createAttributes().build());
    //    DefaultAttributes.put(ModRegistry.GOLEM.get(), FriendGolem.createAttributes().build());
    //    DefaultAttributes.put(ModRegistry.GUARDENTITY.get(), GuardVindicator.createAttributes().build());
  }

  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(GOLEM.get(), FriendGolemRenderer::new);
    event.registerEntityRenderer(GUARDENTITY.get(), GuardRender::new);
    //    RenderingRegistry.registerEntityRenderingHandler(ModRegistry.GOLEM.get(), FriendGolem.CactusGolemRenderer::new);
    //    RenderingRegistry.registerEntityRenderingHandler(ModRegistry.GUARDENTITY.get(), GuardVindicator.GuardRender::new);
  }
}
