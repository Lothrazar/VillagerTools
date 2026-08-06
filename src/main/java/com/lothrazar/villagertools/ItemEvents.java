package com.lothrazar.villagertools;

import com.lothrazar.library.events.EventFlib;
import com.lothrazar.villagertools.entities.FriendGolem;
import com.lothrazar.villagertools.entities.GuardVindicator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.TraderLlama;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class ItemEvents extends EventFlib {

  @SubscribeEvent
  public void onLivingAttackEvent(LivingIncomingDamageEvent event) {
    if (event.getEntity() instanceof GuardVindicator && event.getSource() != null && event.getSource().getDirectEntity() instanceof IronGolem) {
      // golem attacked the thing
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(EntityTickEvent.Pre event) {
    if (event.getEntity() instanceof IronGolem) {
      IronGolem golem = (IronGolem) event.getEntity();
      if (golem.getTarget() instanceof GuardVindicator) {
        AngerUtils.makeCalmGolem(golem);
      }
    }
  }

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getPos() == null || event.getFace() == null) {
      return;
    }
    BlockPos pos = event.getPos().relative(event.getFace());
    Player player = event.getEntity();
    ItemStack stack = event.getItemStack();
    if (player.getCooldowns().isOnCooldown(stack)) {
      return;
    }
    Level world = player.level();
    if (stack.getItem() == VillagerToolsRegistry.BADGE.get()) {
      Pillager child = EntityType.PILLAGER.create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      child.setPos(pos.getX(), pos.getY(), pos.getZ());
      child.setHomeTo(pos, world.getRandom().nextInt(20) + 10);
      world.addFreshEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      child.setPos(pos.getX() + world.getRandom().nextInt(5), pos.getY(), pos.getZ() + world.getRandom().nextInt(5));
      child.setHomeTo(pos, world.getRandom().nextInt(20) + 10);
      world.addFreshEntity(child);
      //another one
      child = EntityType.PILLAGER.create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      child.setPos(pos.getX() + world.getRandom().nextInt(5), pos.getY(), pos.getZ() + world.getRandom().nextInt(5));
      child.setHomeTo(pos, world.getRandom().nextInt(20) + 10);
      world.addFreshEntity(child);
      this.onComplete(player, event.getHand(), stack, true);
    }
  }

  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.EntityInteract event) {
    if (event.getLevel().isClientSide()) {
      return; // Cannot load Villager offers on the client
    }
    ItemStack stack = event.getItemStack();
    Player player = event.getEntity();
    if (player.getCooldowns().isOnCooldown(stack)) {
      return;
    }
    Level world = player.level();
    Entity targetEnt = event.getTarget();
    EntityType<?> targetType = targetEnt.getType();
    BlockPos pos = targetEnt.blockPosition();
    if (stack.getItem() == VillagerToolsRegistry.CURE.get() && targetType == EntityType.ZOMBIE_VILLAGER) {
      ZombieVillager trader = (ZombieVillager) targetEnt;
      //convert as normal
      trader.startConverting(player.getUUID(), world.getRandom().nextInt(2401) + 3600);
      this.onComplete(player, event.getHand(), stack,true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.CONTRACT.get() && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it
      Villager villagerChild = trader.convertTo(EntityType.VILLAGER, ConversionParams.single(trader, false, false), converted -> {});
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack,true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.GEARS.get() && targetType == EntityType.IRON_GOLEM) {
      IronGolem trader = (IronGolem) targetEnt;
      //do it
      FriendGolem villagerChild = VillagerToolsRegistry.GOLEM.get().create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack,true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.DARKNESS.get() && targetEnt instanceof WanderingTrader) {
      WanderingTrader trader = (WanderingTrader) targetEnt;
      //do it
      Evoker villagerChild = trader.convertTo(EntityType.EVOKER, ConversionParams.single(trader, false, false), converted -> {});
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack,true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.DARKNESS.get() && targetEnt instanceof Cow) {
      Cow trader = (Cow) targetEnt;
      //do it
      Ravager villagerChild = trader.convertTo(EntityType.RAVAGER, ConversionParams.single(trader, false, false), converted -> {});
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack,true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.DARKNESS.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      //do it
      Witch villagerChild = vil.convertTo(EntityType.WITCH, ConversionParams.single(vil, false, false), converted -> {});
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.DARKNESS.get() && targetType == EntityType.PILLAGER) {
      Pillager vil = (Pillager) targetEnt;
      //do it
      Evoker villagerChild = vil.convertTo(EntityType.EVOKER, ConversionParams.single(vil, false, false), converted -> {});
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, vil);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.DARKNESS.get() && targetType == VillagerToolsRegistry.GUARDENTITY.get()) {
      //guard REVERSO
      GuardVindicator trader = (GuardVindicator) targetEnt;
      //make it back into pillager
      Pillager child = EntityType.PILLAGER.create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      child.setPos(pos.getX(), pos.getY(), pos.getZ());
      child.setHomeTo(pos, world.getRandom().nextInt(20) + 10);
      world.addFreshEntity(child);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.GUARD_ITEM.get() && targetEnt instanceof Raider) {
      //pillager into guard
      Raider trader = (Raider) targetEnt;
      GuardVindicator villagerChild = VillagerToolsRegistry.GUARDENTITY.get().create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.setHomeTo(pos, 30);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.GUARD_ITEM.get() && targetType == EntityType.WITCH) {
      Witch trader = (Witch) targetEnt;
      //witch can get cured too
      GuardVindicator villagerChild = VillagerToolsRegistry.GUARDENTITY.get().create(world, EntitySpawnReason.SPAWN_ITEM_USE);
      villagerChild.setPos(pos.getX(), pos.getY(), pos.getZ());
      villagerChild.setHomeTo(pos, 30);
      world.addFreshEntity(villagerChild);
      //remove the other
      removeEntity(world, trader);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.KEY.get() && targetType == EntityType.TRADER_LLAMA) {
      TraderLlama tradeLlama = (TraderLlama) targetEnt;
      //do it
      Llama llamaChild = tradeLlama.convertTo(EntityType.LLAMA, ConversionParams.single(tradeLlama, false, false), converted -> {});
      world.addFreshEntity(llamaChild);
      //remove the other
      removeEntity(world, tradeLlama);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.RESTOCK.get() && targetType == EntityType.VILLAGER) {
      //
      Villager vil = (Villager) targetEnt;
      restock(vil);
      this.onComplete(player, event.getHand(), stack, true);
    }
    else if (stack.getItem() == VillagerToolsRegistry.FORGET.get() && targetType == EntityType.VILLAGER) {
      //
      Villager vil = (Villager) targetEnt;

      vil.setVillagerData(vil.getVillagerData().withProfession(world.registryAccess(), VillagerProfession.NONE).withLevel(0));
      // this one does not get consumed hence false
      this.onComplete(player, event.getHand(), stack, false);
    }
    else if (stack.getItem() == VillagerToolsRegistry.KNOWLEDGE.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      //apprentice = 2
      //journeyman = 3
      //expert = 4
      //master = 5
      //     ModMain.LOGGER.info(" beforelevel " + vil.getVillagerData().level());
      //expert is ?
      int level = vil.getVillagerData().level();
      if (level < 5 && world instanceof ServerLevel serverLevel) {
        vil.increaseMerchantCareer(serverLevel);
        //          vil.setVillagerData(vil.getVillagerData().withLevel(level + 1));
        this.onComplete(player, event.getHand(), stack, true);
        //    ModMain.LOGGER.info(" after l " + vil.getVillagerData().level());
      }
    }
    else if (stack.getItem() == VillagerToolsRegistry.BRIBE.get() && targetType == EntityType.VILLAGER) {
      Villager vil = (Villager) targetEnt;
      if (vil.getPlayerReputation(player) < 100) {
        if (!world.isClientSide()) {
          int before = vil.getPlayerReputation(player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          vil.onReputationEventFrom(ReputationEventType.TRADE, player);
          int diff = vil.getPlayerReputation(player) - before;
          //   ModMain.LOGGER.info(" reputation after " + vil.getPlayerReputation(player));
          MutableComponent t = Component.translatable(stack.getItem().getDescriptionId() + ".rep");
          t.append(diff + "");
          player.sendSystemMessage(t);
        }
        this.onComplete(player, event.getHand(), stack, true);
      }
    }
  }

  private void removeEntity(Level world, Entity trader) {
    if (world instanceof ServerLevel level) {
      //despawn
      //      trader.kill();
      trader.remove(Entity.RemovalReason.DISCARDED);
    }
  }

  private void onComplete(Player player, InteractionHand hand, ItemStack stack, boolean consumeItem) {
    player.swing(hand);
    player.getCooldowns().addCooldown(stack, 30);
    if (player.level().isClientSide()) {
      player.sendSystemMessage(Component.translatable(stack.getItem().getDescriptionId() + ".used"));
    }
    if (consumeItem && !player.isCreative()) {
      stack.shrink(1);
    }
  }

  private void restock(Villager vil) {
    vil.restock();
    CompoundTag compound = new CompoundTag();
    compound.putLong("LastRestock", 0);
    ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, vil.level().registryAccess(), compound);
    vil.readAdditionalSaveData(input);
  }

  @SubscribeEvent
  public void onVillagerStart(EntityJoinLevelEvent event) {
    if (event.getEntity() instanceof Villager) {
      tryAddAi((Villager) event.getEntity());
    }
  }

  private void tryAddAi(Villager vil) {
    if (vil.goalSelector.availableGoals.stream().anyMatch((g) -> g.getGoal() instanceof TemptGoal)) {
      return; //already has
    }
    try { // i must be new, i don't have
      vil.goalSelector.addGoal(2, new TemptGoal(vil, 0.666, Ingredient.of(VillagerToolsRegistry.LURE.get()), false));
    }
    catch (Exception e) {
      //don't
    }
  }
}
