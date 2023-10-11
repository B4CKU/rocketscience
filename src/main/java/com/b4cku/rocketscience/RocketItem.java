package com.b4cku.rocketscience;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RocketItem extends Item {

    private final int COOLDOWN_IN_TICKS = 20;
    private final float ROCKET_INITIAL_SPEED = 1.5f;

    public RocketItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 1F); // plays a globalSoundEvent
		user.getItemCooldownManager().set(this, COOLDOWN_IN_TICKS);
        if (!world.isClient) {
            RocketEntity rocketEntity = new RocketEntity(world, user);
            rocketEntity.setItem(itemStack);
            float pitch = user.getPitch();
            float yaw = user.getYaw();
            rocketEntity.setVelocity(user, pitch, yaw, 0.0F, ROCKET_INITIAL_SPEED, 0F);
            world.spawnEntity(rocketEntity); // spawns entity

            //because for technical reasons rockets fly backwards
            rocketEntity.setPitch(pitch + 180.0F);
            rocketEntity.setYaw(yaw + 180.0F);

            user.startRiding(rocketEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

}
