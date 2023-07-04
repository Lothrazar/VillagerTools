package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.FriendGolemRenderer;
import com.lothrazar.villagertools.entities.GuardRender;
import com.lothrazar.villagertools.entities.GuardVindicator;
import com.lothrazar.villagertools.item.ItemVillager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VillagerToolsRegistry {

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VillagerToolsMod.MODID);
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VillagerToolsMod.MODID);
  private static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(VillagerToolsMod.MODID, "tab"));

  @SubscribeEvent
  public static void onCreativeModeTabRegister(RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(TAB, CreativeModeTab.builder().icon(() -> new ItemStack(LURE.get()))
          .title(Component.translatable("itemGroup." + VillagerToolsMod.MODID))
          .displayItems((enabledFlags, populator) -> {
            for (RegistryObject<Item> entry : ITEMS.getEntries()) {
              populator.accept(entry.get());
            }
          }).build());
    });
  }

  public static final RegistryObject<Item> LURE = ITEMS.register("lure", () -> new ItemVillager(new Item.Properties().stacksTo(1)));
  public static final RegistryObject<Item> GEARS = ITEMS.register("gears", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> BRIBE = ITEMS.register("bribe", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> RESTOCK = ITEMS.register("restock", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> FORGET = ITEMS.register("forget", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> CONTRACT = ITEMS.register("contract", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> DARKNESS = ITEMS.register("darkness", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> GUARD_ITEM = ITEMS.register("guard", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> KNOWLEDGE = ITEMS.register("knowledge", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> KEY = ITEMS.register("key", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> BADGE = ITEMS.register("badge", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  public static final RegistryObject<Item> CURE = ITEMS.register("cure", () -> new ItemVillager(new Item.Properties().stacksTo(64)));
  //
  public static final RegistryObject<EntityType<GuardVindicator>> GUARDENTITY = ENTITIES.register("guard", () -> register("guard",
      EntityType.Builder.<GuardVindicator> of(GuardVindicator::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));
  public static final RegistryObject<EntityType<FriendGolem>> GOLEM = ENTITIES.register("reinforced_golem", () -> register("reinforced_golem",
      EntityType.Builder.<FriendGolem> of(FriendGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));

  public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(id);
  }

  @SubscribeEvent
  public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
    event.put(VillagerToolsRegistry.GOLEM.get(), FriendGolem.createAttributes().build());
    event.put(VillagerToolsRegistry.GUARDENTITY.get(), GuardVindicator.createAttributes().build());
  }

  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(GOLEM.get(), FriendGolemRenderer::new);
    event.registerEntityRenderer(GUARDENTITY.get(), GuardRender::new);
  }
}
