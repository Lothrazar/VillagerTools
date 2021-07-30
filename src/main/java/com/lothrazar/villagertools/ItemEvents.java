package com.lothrazar.villagertools;

import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.GuardVindicator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getPos() == null || event.getFace() == null) {
      return;
    }
    BlockPos pos = event.getPos().relative(event.getFace());
    Player player = event.getPlayer();
    ItemStack stack = event.getItemStack();
    if (player.getCooldowns().isOnCooldown(stack.getItem())) {
      return;
    }
    Level world = player.level;
    if (stack.getItem() == ModRegistry.BADGE.get()) {
      Pillager child = EntityType.PILLAGER.create(world);
      child.setPos(pos.getX(), pos.getY(), pos.getZ());
      child.restrictTo(pos, world.random.nextInt(20) + 10);
      world.addFreshEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world);
      child.setPos(pos.getX() + world.random.nextInt(5), pos.getY(), pos.getZ() + world.random.nextInt(5));
      child.restrictTo(pos, world.random.nextInt(20) + 10);
      world.addFreshEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world);
      child.setPos(pos.getX() + world.random.nextInt(5), pos.getY(), pos.getZ() + world.random.nextInt(5));
      child.restrictTo(pos, world.random.nextInt(20) + 10);
      world.addFreshEntity(child);
      this.onComplete(player, event.getHand(), stack);
    }
  }

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.EntityInteract event) {
    ItemStack stack = event.getItemStack();
    Player player = event.getPlayer();
    if (player.getCooldowns().isOnCooldown(stack.getItem())) {
      return;
    }
    Level world = player.level;
    Entity targetEnt = event.getTarget();
    EntityType<?> targetType = targetEnt.getType();
    BlockPos pos = targetEnt.blockPosition();
    if (stack.getItem() == ModRegistry.CURE.get() && targetType == EntityType.ZOMBIE_VILLAGER) {
      ZombieVillager trader = (ZombieVillager) targetEnt;
      //convert as normal
      trader.startConverting(player.getUUID(), world.random.nextInt(2401) + 3600);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.CONTRACT.get() && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it 
      Villager villagerChild = trader.convertTo(EntityType.VILLAGER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GEARS.get() && targetType == EntityType.IRON_GOLEM) {
      IronGolem trader = (IronGolem) targetEnt;
      //do it 
      FriendGolem villagerChild = ModRegistry.GOLEM.get().create(world);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it 
      Evoker villagerChild = trader.convertTo(EntityType.EVOKER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it 
      Evoker villagerChild = trader.convertTo(EntityType.EVOKER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.WANDERING_TRADER
        && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it
      Illusioner villagerChild = trader.convertTo(EntityType.ILLUSIONER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetEnt instanceof Cow) {
      Cow trader = (Cow) targetEnt;
      //do it 
      Ravager villagerChild = trader.convertTo(EntityType.RAVAGER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      //do it 
      Witch villagerChild = vil.convertTo(EntityType.WITCH, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == EntityType.PILLAGER) {
      Pillager vil = (Pillager) targetEnt;
      //do it 
      Evoker villagerChild = vil.convertTo(EntityType.EVOKER, false);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.DARKNESS.get() && targetType == ModRegistry.GUARDENTITY.get()) {
      //guard REVERSO
      GuardVindicator trader = (GuardVindicator) targetEnt;
      //make it back into pillager
      Pillager child = EntityType.PILLAGER.create(world);
      child.setPos(pos.getX(), pos.getY(), pos.getZ());
      child.restrictTo(pos, world.random.nextInt(20) + 10);
      world.addFreshEntity(child);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GUARD_ITEM.get() && targetEnt instanceof Raider) {
      //pillager into guard
      Raider trader = (Raider) targetEnt;
      GuardVindicator villagerChild = ModRegistry.GUARDENTITY.get().create(world);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.restrictTo(pos, 30);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.GUARD_ITEM.get() && targetType == EntityType.WITCH) {
      Witch trader = (Witch) targetEnt;
      //witch can get cured too
      GuardVindicator villagerChild = ModRegistry.GUARDENTITY.get().create(world);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.restrictTo(pos, 30);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KEY.get() && targetType == EntityType.TRADER_LLAMA) {
      TraderLlama tradeLlama = (TraderLlama) targetEnt;
      //do it 
      Llama llamaChild = tradeLlama.convertTo(EntityType.LLAMA, false);
      world.addFreshEntity(llamaChild);
      //remove the other
      removeEntity(world, tradeLlama);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.RESTOCK.get() && targetType == EntityType.VILLAGER) {
      //
      Villager vil = (Villager) targetEnt;
      restock(vil);
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.FORGET.get() && targetType == EntityType.VILLAGER) {
      // 
      Villager vil = (Villager) targetEnt;
      // ModMain.LOGGER.info("forget trades on " + vil.getVillagerData());
      vil.setVillagerData(vil.getVillagerData().setProfession(VillagerProfession.NONE).setLevel(0));
      //
      this.onComplete(player, event.getHand(), stack);
    }
    else if (stack.getItem() == ModRegistry.KNOWLEDGE.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      //apprentice = 2
      //journeyman = 3
      //expert = 4
      //master = 5
      //     ModMain.LOGGER.info(" beforelevel " + vil.getVillagerData().getLevel());
      //expert is ?
      int level = vil.getVillagerData().getLevel();
      if (level < 5) {
        vil.increaseMerchantCareer();
        //          vil.setVillagerData(vil.getVillagerData().withLevel(level + 1));
        this.onComplete(player, event.getHand(), stack);
        //    ModMain.LOGGER.info(" after l " + vil.getVillagerData().getLevel());
      }
    }
    else if (stack.getItem() == ModRegistry.BRIBE.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      if (vil.getPlayerReputation(player) < 100) {
        if (!world.isClientSide) {
          int before = vil.getPlayerReputation(player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          int diff = vil.getPlayerReputation(player) - before;
          //   ModMain.LOGGER.info(" reputation after " + vil.getPlayerReputation(player));
          TranslatableComponent t = new TranslatableComponent(stack.getDescriptionId() + ".rep");
          t.append(diff + "");
          player.displayClientMessage(t, false);
        }
        this.onComplete(player, event.getHand(), stack);
      }
    }
  }

  private void removeEntity(Level world, Entity trader) {
    if (world instanceof ServerLevel) {
      //despawn
      ((ServerLevel) world).removeEntity(trader);
    }
  }

  private void onComplete(Player player, InteractionHand hand, ItemStack stack) {
    player.swing(hand);
    player.getCooldowns().addCooldown(stack.getItem(), 30);
    if (player.level.isClientSide) {
      player.displayClientMessage(new TranslatableComponent(stack.getDescriptionId() + ".used"), false);
    }
    if (!player.isCreative()) {
      stack.shrink(1);
    }
  }

  private void restock(Villager vil) {
    vil.restock();
    CompoundTag compound = new CompoundTag();
    compound.putLong("LastRestock", 0);
    vil.readAdditionalSaveData(compound);
  }

  @SubscribeEvent
  public void onVillagerStart(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof Villager) {
      tryAddAi((Villager) event.getEntity());
    }
  }

  private void tryAddAi(Villager vil) {
    if (vil.goalSelector.availableGoals.stream().anyMatch((g) -> g.getGoal() instanceof TemptGoal)) {
      return; //already has 
    }
    try { // i must be new, i don't have 
      vil.goalSelector.addGoal(2, new TemptGoal(vil, 0.666, Ingredient.of(ModRegistry.LURE.get()), false));
    }
    catch (Exception e) {
      //don't 
    }
  }
}
