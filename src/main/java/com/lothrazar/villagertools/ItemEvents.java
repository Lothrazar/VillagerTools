package com.lothrazar.villagertools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {
  //town guard
  //ravager rideable (saddle)

  RavagerEntity r;
  //evoker
  EvokerEntity e;
  //pillager
  PillagerEntity p;
  //vindicator
  VindicatorEntity v;

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    BlockPos pos = event.getPos();
    PlayerEntity player = event.getPlayer();
    ItemStack stack = event.getItemStack();
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    World world = player.world;
    if (stack.getItem() == ModRegistry.BADGE.get()) {
      PillagerEntity child = EntityType.PILLAGER.create(world);
      child.setPosition(pos.getX(), pos.getY(), pos.getZ());
      child.setHomePosAndDistance(pos, world.rand.nextInt(20) + 10);
      world.addEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world);
      child.setPosition(pos.getX(), pos.getY(), pos.getZ());
      child.setHomePosAndDistance(pos, world.rand.nextInt(20) + 10);
      world.addEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world);
      child.setPosition(pos.getX(), pos.getY(), pos.getZ());
      child.setHomePosAndDistance(pos, world.rand.nextInt(20) + 10);
      world.addEntity(child);
      this.onComplete(player, event.getHand(), stack);
    }
  }

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.EntityInteract event) {
    ItemStack stack = event.getItemStack();
    PlayerEntity player = event.getPlayer();
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    World world = player.world;
    Entity targetEnt = event.getTarget();
    EntityType<?> targetType = targetEnt.getType();
    if (stack.getItem() == ModRegistry.CURE.get() && targetType == EntityType.ZOMBIE_VILLAGER) {
      ZombieVillagerEntity trader = (ZombieVillagerEntity) targetEnt;
      //convert as normal
      trader.startConverting(player.getUniqueID(), world.rand.nextInt(2401) + 3600);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.CONTRACT.get() && targetEnt instanceof WanderingTraderEntity) {
      WanderingTraderEntity trader = (WanderingTraderEntity) targetEnt;
      //do it 
      VillagerEntity villagerChild = trader.func_233656_b_(EntityType.VILLAGER, false);
      world.addEntity(villagerChild);
      //remove the other
      if (world instanceof ServerWorld) {
        ((ServerWorld) world).removeEntity(trader);
      }
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof WanderingTraderEntity) {
      WanderingTraderEntity trader = (WanderingTraderEntity) targetEnt;
      //do it 
      EvokerEntity villagerChild = trader.func_233656_b_(EntityType.EVOKER, false);
      world.addEntity(villagerChild);
      //remove the other
      if (world instanceof ServerWorld) {
        ((ServerWorld) world).removeEntity(trader);
      }
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof CowEntity) {
      CowEntity trader = (CowEntity) targetEnt;
      //do it 
      RavagerEntity villagerChild = trader.func_233656_b_(EntityType.RAVAGER, false);
      world.addEntity(villagerChild);
      //remove the other
      if (world instanceof ServerWorld) {
        ((ServerWorld) world).removeEntity(trader);
      }
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof VillagerEntity) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      //do it 
      WitchEntity villagerChild = vil.func_233656_b_(EntityType.WITCH, false);
      world.addEntity(villagerChild);
      //remove the other
      if (world instanceof ServerWorld) {
        ((ServerWorld) world).removeEntity(vil);
      }
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KEY.get() && targetEnt instanceof TraderLlamaEntity) {
      TraderLlamaEntity tradeLlama = (TraderLlamaEntity) targetEnt;
      //do it 
      LlamaEntity llamaChild = tradeLlama.func_233656_b_(EntityType.LLAMA, false);
      world.addEntity(llamaChild);
      //remove the other
      if (world instanceof ServerWorld) {
        ((ServerWorld) world).removeEntity(tradeLlama);
      }
      this.onComplete(player, event.getHand(), stack);
    }
    //try all
    if (stack.getItem() == ModRegistry.RESTOCK.get() && targetEnt instanceof VillagerEntity) {
      //
      VillagerEntity vil = (VillagerEntity) targetEnt;
      ModMain.LOGGER.info("Reset trades on " + vil.getVillagerData().getProfession());
      ModMain.LOGGER.info("Reset trades on " + vil.getVillagerData().getType());
      ModMain.LOGGER.info(" level " + vil.getVillagerData().getLevel());
      restock(vil);
    }
    else if (stack.getItem() == ModRegistry.FORGET.get() && targetEnt instanceof VillagerEntity) {
      // 
      VillagerEntity vil = (VillagerEntity) targetEnt;
      ModMain.LOGGER.info("forget trades on " + vil.getVillagerData());
      vil.setVillagerData(vil.getVillagerData().withProfession(VillagerProfession.NONE).withLevel(0));
      //
      //      this.setVillagerData(this.getVillagerData().withProfession(Registry.VILLAGER_PROFESSION.getRandom(this.rand)));
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KNOWLEDGE.get() && targetEnt instanceof VillagerEntity) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      // TODO : find max
      //apprentice = 2
      //journeyman = 3
      //expert = 4
      //master = 5
      ModMain.LOGGER.info(" beforelevel " + vil.getVillagerData().getLevel());
      //expert is ?
      int level = vil.getVillagerData().getLevel();
      if (level < 5) {
        vil.levelUp();
        //          vil.setVillagerData(vil.getVillagerData().withLevel(level + 1));
        this.onComplete(player, event.getHand(), stack);
        ModMain.LOGGER.info(" after l " + vil.getVillagerData().getLevel());
      }
    }
    else if (stack.getItem() == ModRegistry.BRIBE.get() && targetEnt instanceof VillagerEntity) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      if (vil.getPlayerReputation(player) < 100) {
        if (!world.isRemote) {
          int before = vil.getPlayerReputation(player);
          vil.updateReputation(IReputationType.TRADE, player);
          vil.updateReputation(IReputationType.TRADE, player);
          vil.updateReputation(IReputationType.TRADE, player);
          int diff = vil.getPlayerReputation(player) - before;
          ModMain.LOGGER.info(" reputation after " + vil.getPlayerReputation(player));
          TranslationTextComponent t = new TranslationTextComponent(stack.getTranslationKey() + ".rep");
          t.appendString(diff + "");
          player.sendStatusMessage(t, false);
        }
        this.onComplete(player, event.getHand(), stack);
      }
    }
    //    else {
    //      this.onFail(player, stack);
    //    }
  }

  private void onFail(PlayerEntity player, ItemStack stack) {
    player.sendStatusMessage(new TranslationTextComponent(stack.getTranslationKey() + ".used"), false);
  }

  private void onComplete(PlayerEntity player, Hand hand, ItemStack stack) {
    player.sendStatusMessage(new TranslationTextComponent(stack.getTranslationKey() + ".used"), false);
    player.swingArm(hand);
    player.getCooldownTracker().setCooldown(stack.getItem(), 30);
    if (!player.isCreative()) {
      stack.shrink(1);
    }
  }

  private void restock(VillagerEntity vil) {
    vil.restock();
    CompoundNBT compound = new CompoundNBT();
    compound.putLong("LastRestock", 0);
    vil.readAdditional(compound);
  }

  @SubscribeEvent
  public void onVillagerStart(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof VillagerEntity) {
      tryAddAi((VillagerEntity) event.getEntity());
    }
  }

  private void tryAddAi(VillagerEntity vil) {
    if (vil.goalSelector.goals.stream().anyMatch((g) -> g.getGoal() instanceof TemptGoal)) {
      return; //already has 
    }
    try { // i must be new, i don't have 
      vil.goalSelector.addGoal(2, new TemptGoal(vil, 0.666, Ingredient.fromItems(ModRegistry.LURE.get()), false));
    }
    catch (Exception e) {
      //don't 
    }
  }
}
