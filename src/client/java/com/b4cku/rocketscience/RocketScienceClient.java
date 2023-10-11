package com.b4cku.rocketscience;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class RocketScienceClient implements ClientModInitializer {

	public static final EntityModelLayer MODEL_ROCKET_LAYER = new EntityModelLayer(new Identifier(RocketScience.MODID, "rocket"), "main");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(RocketScience.ROCKET_ENTITY_ENTITY_TYPE, RocketEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_ROCKET_LAYER, RocketEntityModel::getTexturedModelData);
	}
}