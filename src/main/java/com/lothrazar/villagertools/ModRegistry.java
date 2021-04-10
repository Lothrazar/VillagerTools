package com.lothrazar.villagertools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

  public static final ItemGroup TAB = new ItemGroup(ModMain.MODID) {

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(LURE.get());
    }
  };
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);
  //
  public static final RegistryObject<Item> LURE = ITEMS.register("lure", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(1).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> BRIBE = ITEMS.register("bribe", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> RESTOCK = ITEMS.register("restock", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> FORGET = ITEMS.register("forget", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> CONTRACT = ITEMS.register("contract", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> DARKNESS = ITEMS.register("darkness", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> KNOWLEDGE = ITEMS.register("knowledge", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> KEY = ITEMS.register("key", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> BADGE = ITEMS.register("badge", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
  public static final RegistryObject<Item> CURE = ITEMS.register("cure", () -> new ItemTradeCheats(new Item.Properties().maxStackSize(64).group(ModRegistry.TAB)));
}
