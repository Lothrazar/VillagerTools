package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.FriendGolemRenderer;
import com.lothrazar.villagertools.entities.GuardRender;
import com.lothrazar.villagertools.entities.GuardVindicator;
import com.lothrazar.villagertools.item.ItemVillager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = VillagerToolsMod.MODID)
public class VillagerToolsRegistry {

  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VillagerToolsMod.MODID);
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, VillagerToolsMod.MODID);
  private static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(VillagerToolsMod.MODID, "tab"));

  @SubscribeEvent
  public static void onCreativeModeTabRegister(RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(TAB, CreativeModeTab.builder().icon(() -> new ItemStack(LURE.get()))
          .title(Component.translatable("itemGroup." + VillagerToolsMod.MODID))
          .displayItems((enabledFlags, populator) -> {
            for (DeferredHolder<Item, ? extends Item> entry : ITEMS.getEntries()) {
              populator.accept(entry.get());
            }
          }).build());
    });
  }

  public static final DeferredHolder<Item, Item>LURE = ITEMS.registerItem("lure", props -> new ItemVillager(props.stacksTo(1)));
  public static final DeferredHolder<Item, Item>GEARS = ITEMS.registerItem("gears", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>BRIBE = ITEMS.registerItem("bribe", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>RESTOCK = ITEMS.registerItem("restock", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>FORGET = ITEMS.registerItem("forget", props -> new ItemVillager(props.stacksTo(1)));
  public static final DeferredHolder<Item, Item>CONTRACT = ITEMS.registerItem("contract", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>DARKNESS = ITEMS.registerItem("darkness", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>GUARD_ITEM = ITEMS.registerItem("guard", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>KNOWLEDGE = ITEMS.registerItem("knowledge", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>KEY = ITEMS.registerItem("key", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>BADGE = ITEMS.registerItem("badge", props -> new ItemVillager(props.stacksTo(64)));
  public static final DeferredHolder<Item, Item>CURE = ITEMS.registerItem("cure", props -> new ItemVillager(props.stacksTo(64)));
  //
  public static final DeferredHolder<EntityType<?>, EntityType<GuardVindicator>> GUARDENTITY = ENTITIES.register("guard", () -> register("guard",
      EntityType.Builder.<GuardVindicator> of(GuardVindicator::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));
  public static final DeferredHolder<EntityType<?>, EntityType<FriendGolem>> GOLEM = ENTITIES.register("reinforced_golem", () -> register("reinforced_golem",
      EntityType.Builder.<FriendGolem> of(FriendGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10)));

  public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
    return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(VillagerToolsMod.MODID, id)));
  }

  @SubscribeEvent
  public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
    event.put(VillagerToolsRegistry.GOLEM.get(), FriendGolem.createAttributes().build());
    event.put(VillagerToolsRegistry.GUARDENTITY.get(), GuardVindicator.createAttributes().build());
  }

  // Villager doesn't normally have this attribute, but ItemEvents.tryAddAi() gives every
  // villager a vanilla TemptGoal (for the Golemsteel Brazier), and TemptGoal.canUse() reads
  // it unconditionally - without this, the goal crashes the server the first tick it runs.
  @SubscribeEvent
  public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
    event.add(EntityType.VILLAGER, Attributes.TEMPT_RANGE);
  }

  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(GOLEM.get(), FriendGolemRenderer::new);
    event.registerEntityRenderer(GUARDENTITY.get(), GuardRender::new);
  }
}
