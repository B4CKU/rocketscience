package com.b4cku.rocketscience;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketScience implements ModInitializer {
	public static final String MODID = "rocketscience";
    public static final Logger LOGGER = LoggerFactory.getLogger("rocketscience");
    public static final Item ROCKET_ITEM = new RocketItem(new FabricItemSettings().maxCount(16));

	public static final EntityType<RocketEntity> ROCKET_ENTITY_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MODID, "rocket"),
			FabricEntityTypeBuilder.<RocketEntity>create(SpawnGroup.MISC, RocketEntity::new)
					.dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the projectile
					.trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
					.build() //no no no we no delete this, fabric-san
	);

	@Override
	public void onInitialize() {
		//TODO: creative item groups support for the rocket - tools & utilities, after boats?

        Registry.register(Registries.ITEM, new Identifier(MODID, "rocket"), ROCKET_ITEM);

	}
}