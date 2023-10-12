package com.b4cku.rocketscience;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RocketEntity extends ThrownItemEntity {

    //i have no clue how to measure it lmao, i just change the value until it's good
    private final float TURNING_SENSITIVITY = 0.15f;

    private final float ROCKET_EXPLOSION_POWER = 1.0f;
    private final float ROCKET_EXPLOSION_POWER_RIDDEN = 2.0f;

    private final int ROCKET_AGE_BIGGER_EXPLOSION = 40;
    private final int ROCKET_AGE_LOW_FUEL = 80;
    private final int ROCKET_AGE_NO_FUEL = 120;
    private final int ROCKET_AGE_EXPLODE = 160;

    private int rocket_age = 0;
    private int rocket_age_ridden = 0;

    public static final float TARGET_ROCKET_SPEED = 1.5f ;

    public RocketEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public RocketEntity(World world, LivingEntity owner) {
        super(RocketScience.ROCKET_ENTITY_ENTITY_TYPE, owner, world);
    }

    public RocketEntity(World world, double x, double y, double z) {
        super(RocketScience.ROCKET_ENTITY_ENTITY_TYPE, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RocketScience.ROCKET_ITEM;
    }

    public void tick() {
        super.tick();

        handleInputs(getVelocity());
        maintainSpeed(getVelocity());

        if (rocket_age_ridden == ROCKET_AGE_BIGGER_EXPLOSION) {
            getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 1F);
        }

        if (hasPilot()) {
            rocket_age_ridden++;
        }
        rocket_age++;

        if (rocket_age > ROCKET_AGE_EXPLODE) {
            blowUp();
        }

        if (this.random.nextInt(2) == 0) {
            //TODO: i should probably move those back to the rocket's exhaust
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    private void maintainSpeed(Vec3d speed_vector) {
        if (rocket_age_ridden > ROCKET_AGE_LOW_FUEL) {
            return;
        }

        if (speed_vector.length() > TARGET_ROCKET_SPEED) {
            return;
        }

        speed_vector = speed_vector.normalize().multiply(TARGET_ROCKET_SPEED);
        setVelocity(speed_vector);

    }

    private void handleInputs(Vec3d target_velocity) {
        LivingEntity pilot = (LivingEntity)this.getFirstPassenger();
        if (pilot == null) {
            return;
        }

        //finally found a way to check pilot's inputs
        //this does NOT check for velocity, it's actually inputs
        float forwardInput = pilot.forwardSpeed;
        //forward = positive
        float sidewaysInput = pilot.sidewaysSpeed;
        //left = positive


        final Vec3d temp = target_velocity.normalize();

        target_velocity = target_velocity.rotateY(sidewaysInput * TURNING_SENSITIVITY);

        target_velocity = target_velocity.rotateX((float)temp.getZ() * forwardInput * TURNING_SENSITIVITY).rotateZ((float)temp.getX() * -forwardInput * TURNING_SENSITIVITY);
        //ihatemyselfihatemyselfihatemyselfihatemyselfihatemyselfihatemyself
        //i will leave the above comment here just as a reminder that i'm an absolute idiot

        this.setVelocity(target_velocity);
    }

    private boolean hasPilot() {
        return this.getFirstPassenger() != null;
    }

    //function below returns a number between 0 and 1, based on how much "fuel" the rocket still has
    private float fuelCheck() {
        if (rocket_age_ridden < ROCKET_AGE_LOW_FUEL) {
            return 1f;
        }
        else if (rocket_age_ridden > ROCKET_AGE_NO_FUEL) {
            return 0f;
        }
        return 1 - ((float) (rocket_age_ridden - ROCKET_AGE_LOW_FUEL) / (ROCKET_AGE_NO_FUEL - ROCKET_AGE_LOW_FUEL));
    }

    protected float getGravity() {
        return 0f + 0.1f * (1 - fuelCheck());
    }

    public double getMountedHeightOffset() {
        return 0.1f;
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
        this.kill(); //i moved it here to remove the crash when it explodes near to another rocket, i hope it doesn't bite me in the ass later
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), rocket_age_ridden > ROCKET_AGE_BIGGER_EXPLOSION ? ROCKET_EXPLOSION_POWER_RIDDEN : ROCKET_EXPLOSION_POWER, false, World.ExplosionSourceType.NONE);
        this.getWorld().sendEntityStatus(this, (byte)3); // particle?
        //i have no clue what the above line does, but it was there in the fabric documentation's entity example
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Age", rocket_age);
        nbt.putInt("AgeRidden", rocket_age_ridden);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        rocket_age = nbt.getInt("Age");
        rocket_age_ridden = nbt.getInt("AgeRidden");
    }
}
