package com.lothrazar.villagertools.entities;

import com.lothrazar.villagertools.VillagerToolsMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VindicatorRenderer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.resources.Identifier;

public class GuardRender extends VindicatorRenderer {

  private static final Identifier TXT = Identifier.fromNamespaceAndPath(VillagerToolsMod.MODID, "textures/entity/guard.png");

  public GuardRender(EntityRendererProvider.Context ctx) {
    super(ctx);
  }

  @Override
  public Identifier getTextureLocation(IllagerRenderState state) {
    return TXT;
  }
}
