package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.GuardVindicator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
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
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

  @SubscribeEvent
  public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {}

  @SubscribeEvent
  public void onLivingAttackEvent(LivingAttackEvent event) {
    if (event.getEntityLiving() instanceof GuardVindicator && event.getSource() != null && event.getSource().getTrueSource() instanceof IronGolemEntity) {
      // golem attacked the thing  
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof IronGolemEntity) {
      IronGolemEntity golem = (IronGolemEntity) event.getEntityLiving();
      if (golem.getAttackTarget() instanceof GuardVindicator) {
        AngerUtils.makeCalmGolem(golem);
      }
    }
  }
  //
  //  @SubscribeEvent
  //  public void onLivingDamageEvent(LivingDamageEvent event) {
  //    if (event.getEntityLiving() instanceof GuardVindicator && event.getSource() != null
  //        && event.getSource().getTrueSource() instanceof IronGolemEntity) {
  //      // golem attacked the thing 
  //      event.setAmount(0);
  //      event.setCanceled(true);
  //    }
  //  }
  //  @SubscribeEvent
  //  public void onLivingHurtEvent(LivingHurtEvent event) {
  //    if (event.getEntityLiving() instanceof GuardVindicator && event.getSource() != null
  //        && event.getSource().getTrueSource() instanceof IronGolemEntity) {
  //      // damage source is the golem attacked the thing
  //      event.setAmount(0);
  //      //      event.setCanceled(true);
  //    }
  //  }

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getPos() == null || event.getFace() == null) {
      return;
    }
    BlockPos pos = event.getPos().offset(event.getFace());
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
      child.setPosition(pos.getX() + world.rand.nextInt(5), pos.getY(), pos.getZ() + world.rand.nextInt(5));
      child.setHomePosAndDistance(pos, world.rand.nextInt(20) + 10);
      world.addEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world);
      child.setPosition(pos.getX() + world.rand.nextInt(5), pos.getY(), pos.getZ() + world.rand.nextInt(5));
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
    BlockPos pos = targetEnt.getPosition();
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
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GEARS.get() && targetType == EntityType.IRON_GOLEM) {
      IronGolemEntity trader = (IronGolemEntity) targetEnt;
      //do it 
      FriendGolem villagerChild = ModRegistry.GOLEM.get().create(world);
      villagerChild.setPosition(pos.getX(), pos.getY(), pos.getZ());
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof WanderingTraderEntity) {
      WanderingTraderEntity trader = (WanderingTraderEntity) targetEnt;
      //do it 
      EvokerEntity villagerChild = trader.func_233656_b_(EntityType.EVOKER, false);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof WanderingTraderEntity) {
      WanderingTraderEntity trader = (WanderingTraderEntity) targetEnt;
      //do it 
      EvokerEntity villagerChild = trader.func_233656_b_(EntityType.EVOKER, false);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.WANDERING_TRADER
        && targetEnt instanceof WanderingTraderEntity) {
          WanderingTraderEntity trader = (WanderingTraderEntity) targetEnt;
          //do it 
          IllusionerEntity villagerChild = trader.func_233656_b_(EntityType.ILLUSIONER, false);
          world.addEntity(villagerChild);
          //remove the other
          removeEntity(world, trader);
          this.onComplete(player, event.getHand(), stack);
        }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof CowEntity) {
      CowEntity trader = (CowEntity) targetEnt;
      //do it 
      RavagerEntity villagerChild = trader.func_233656_b_(EntityType.RAVAGER, false);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.VILLAGER) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      //do it 
      WitchEntity villagerChild = vil.func_233656_b_(EntityType.WITCH, false);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.PILLAGER) {
      PillagerEntity vil = (PillagerEntity) targetEnt;
      //do it 
      EvokerEntity villagerChild = vil.func_233656_b_(EntityType.EVOKER, false);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == ModRegistry.GUARDENTITY.get()) {
      //guard REVERSO
      GuardVindicator trader = (GuardVindicator) targetEnt;
      //make it back into pillager
      PillagerEntity child = EntityType.PILLAGER.create(world);
      child.setPosition(pos.getX(), pos.getY(), pos.getZ());
      child.setHomePosAndDistance(pos, world.rand.nextInt(20) + 10);
      world.addEntity(child);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GUARD_ITEM.get() && targetEnt instanceof AbstractRaiderEntity) {
      //pillager into guard
      AbstractRaiderEntity trader = (AbstractRaiderEntity) targetEnt;
      GuardVindicator villagerChild = ModRegistry.GUARDENTITY.get().create(world);
      villagerChild.setPosition(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.setHomePosAndDistance(pos, 30);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GUARD_ITEM.get() && targetType == EntityType.WITCH) {
      WitchEntity trader = (WitchEntity) targetEnt;
      //witch can get cured too
      GuardVindicator villagerChild = ModRegistry.GUARDENTITY.get().create(world);
      villagerChild.setPosition(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.setHomePosAndDistance(pos, 30);
      world.addEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KEY.get() && targetType == EntityType.TRADER_LLAMA) {
      TraderLlamaEntity tradeLlama = (TraderLlamaEntity) targetEnt;
      //do it 
      LlamaEntity llamaChild = tradeLlama.func_233656_b_(EntityType.LLAMA, false);
      world.addEntity(llamaChild);
      //remove the other
      removeEntity(world, tradeLlama);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.RESTOCK.get() && targetType == EntityType.VILLAGER) {
      //
      VillagerEntity vil = (VillagerEntity) targetEnt;
      restock(vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.FORGET.get() && targetType == EntityType.VILLAGER) {
      // 
      VillagerEntity vil = (VillagerEntity) targetEnt;
      // ModMain.LOGGER.info("forget trades on " + vil.getVillagerData());
      vil.setVillagerData(vil.getVillagerData().withProfession(VillagerProfession.NONE).withLevel(0));
      //
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KNOWLEDGE.get() && targetType == EntityType.VILLAGER) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      //apprentice = 2
      //journeyman = 3
      //expert = 4
      //master = 5
      //     ModMain.LOGGER.info(" beforelevel " + vil.getVillagerData().getLevel());
      //expert is ?
      int level = vil.getVillagerData().getLevel();
      if (level < 5) {
        vil.levelUp();
        //          vil.setVillagerData(vil.getVillagerData().withLevel(level + 1));
        this.onComplete(player, event.getHand(), stack);
        //    ModMain.LOGGER.info(" after l " + vil.getVillagerData().getLevel());
      }
    }
    else if (stack.getItem() == ModRegistry.BRIBE.get() && targetType == EntityType.VILLAGER) {
      VillagerEntity vil = (VillagerEntity) targetEnt;
      if (vil.getPlayerReputation(player) < 100) {
        if (!world.isRemote) {
          int before = vil.getPlayerReputation(player);
          vil.updateReputation(IReputationType.TRADE, player);
          vil.updateReputation(IReputationType.TRADE, player);
          vil.updateReputation(IReputationType.TRADE, player);
          int diff = vil.getPlayerReputation(player) - before;
          //   ModMain.LOGGER.info(" reputation after " + vil.getPlayerReputation(player));
          TranslationTextComponent t = new TranslationTextComponent(stack.getTranslationKey() + ".rep");
          t.appendString(diff + "");
          player.sendStatusMessage(t, false);
        }
        this.onComplete(player, event.getHand(), stack);
      }
    }
  }

  private void removeEntity(World world, Entity trader) {
    if (world instanceof ServerWorld) {
      ((ServerWorld) world).removeEntity(trader);
    }
  }

  private void onComplete(PlayerEntity player, Hand hand, ItemStack stack) {
    player.swingArm(hand);
    player.getCooldownTracker().setCooldown(stack.getItem(), 30);
    if (player.world.isRemote) {
      player.sendStatusMessage(new TranslationTextComponent(stack.getTranslationKey() + ".used"), false);
    }
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
