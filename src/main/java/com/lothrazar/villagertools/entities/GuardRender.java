package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.VillagerToolsMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VindicatorRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vindicator;
@SuppressWarnings("hiding")
public class GuardRender<GuardVindicator> extends VindicatorRenderer {

  private static final ResourceLocation TXT = new ResourceLocation(VillagerToolsMod.MODID, "textures/entity/guard.png");

  public GuardRender(EntityRendererProvider.Context ctx) {
    super(ctx);
  }

  @Override
  public ResourceLocation getTextureLocation(Vindicator entity) {
    return TXT;
  }
}
