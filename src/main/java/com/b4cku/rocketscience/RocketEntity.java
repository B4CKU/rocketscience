package com.b4cku.rocketscience;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class RocketEntity extends ThrownItemEntity {

    private final float ROCKET_EXPLOSION_POWER = 2.0f;

    public RocketEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public RocketEntity(World world, LivingEntity owner) {
        super(RocketScience.ROCKET_ENTITY_ENTITY_TYPE, owner, world); // null will be changed later
    }

    public RocketEntity(World world, double x, double y, double z) {
        super(RocketScience.ROCKET_ENTITY_ENTITY_TYPE, x, y, z, world); // null will be changed later
    }

    @Override
    protected Item getDefaultItem() {
        return RocketScience.ROCKET_ITEM;
    }


    public void tick() {
        super.tick();
        if (this.random.nextInt(3) == 0) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);

    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) { // checks if the world is client
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), ROCKET_EXPLOSION_POWER, false, World.ExplosionSourceType.NONE);
            this.getWorld().sendEntityStatus(this, (byte)3); // particle?
            this.kill(); // kills the projectile
        }

    }

}
