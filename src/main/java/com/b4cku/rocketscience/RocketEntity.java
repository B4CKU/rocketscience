package com.b4cku.rocketscience;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RocketEntity extends ThrownItemEntity {

    private final float ROCKET_EXPLOSION_POWER = 2.0f;

    //i have no clue how to measure it lmao, i just change the value until it's good
    private final float TURNING_SENSITIVITY = 0.07f;

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
        handleInputs();
        if (this.random.nextInt(3) == 0) {
            //TODO: i should probably move those back to the rocket's exhaust
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    private void handleInputs() {
        LivingEntity pilot = (LivingEntity)this.getFirstPassenger();
        if (pilot == null) {
            return;
        }

        //finally found a way to check pilot's inputs
        //this does NOT check for velocity, it's actually inputs
        float fw = pilot.forwardSpeed;
        //forward = positive
        float sw = pilot.sidewaysSpeed;
        //left = positive

        Vec3d target_velocity = this.getVelocity().rotateY(sw * TURNING_SENSITIVITY).rotateX(fw * TURNING_SENSITIVITY);
        this.setVelocity(target_velocity);

        //System.out.print("fw:"+fw+" sw:"+sw+"\n");
    }

    protected float getGravity() {
        /*LivingEntity pilot = (LivingEntity)this.getFirstPassenger();
        if (pilot == null) {
            return 0f;
        }*/
        //i really want to add that, but i'm afraid it'll be really fucking heavy on the physics engine and will tank the performance
        //think how many times per second gravity calculations are performed, do we really want to check for a passenger this many times per second?


        return 0.02F;
    }

    //i'm fairly certain this does something, but my IDE is goofing and colors it gray. i yoinked it from some fireball code
    protected float getDrag() {
        return 0.95F;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) { // checks if the world is client
            blowUp();
        }

    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            blowUp();
            return true;
        }
    }

    private void blowUp() {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), ROCKET_EXPLOSION_POWER, false, World.ExplosionSourceType.NONE);
        this.getWorld().sendEntityStatus(this, (byte)3); // particle?
        this.kill(); // kills the projectile
    }

}
