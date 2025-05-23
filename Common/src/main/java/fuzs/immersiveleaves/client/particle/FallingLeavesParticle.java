package fuzs.immersiveleaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.immersiveleaves.client.world.level.ParticleCollisionHelper;
import fuzs.immersiveleaves.config.ClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FallingLeavesParticle extends TerrainParticle {
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0);

    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(1.0F, 0.0F, 0.95F, 1.0F);
    private final float particleRandom;
    private final float spinAcceleration;
    private final double windStrength;
    private final float windDirectionOffset;
    private final DecayBehavior decayBehavior;
    private float rotSpeed;
    private boolean stoppedByCollision;

    public FallingLeavesParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
        this.setParticleSpeed(0.0, 0.0, 0.0);
        this.setAlpha(this.lifetimeAlpha.startAlpha());
        this.setSize(0.2F, 0.2F);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.rotSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
        this.particleRandom = this.random.nextFloat();
        this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.lifetime = ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.particleLifetime * 20;
        this.gravity = 7.5E-4F;
        this.quadSize = this.random.nextBoolean() ? 0.05F : 0.075F;
        this.setSize(this.quadSize, this.quadSize);
        this.friction = 1.0F;
        this.windStrength = ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.windStrength *
                this.getWindStrengthMultiplier(level.isRaining(), level.isThundering());
        this.windDirectionOffset = (float) ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.windDirection;
        this.decayBehavior = ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.decayBehavior;
    }

    protected double getWindStrengthMultiplier(boolean raining, boolean thundering) {
        if (ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.weatherEffects) {
            return raining && thundering ? 4.0 : (raining || thundering ? 2.0 : 1.0);
        } else {
            return 1.0;
        }
    }

    protected float getNormalizedAge() {
        return Mth.clamp(this.age / (float) this.lifetime, 0.0F, 1.0F);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        this.setAlpha(this.lifetimeAlpha.currentAlphaForAge(this.age, this.lifetime, partialTicks));
        super.render(buffer, camera, partialTicks);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.tickAge();
        if (this.age >= this.lifetime) {
            this.remove();
        }

        if (!this.removed) {
            float normalizedAge = this.getNormalizedAge();
            double d = Math.cos(Math.toRadians(this.particleRandom * 60.0F + this.windDirectionOffset)) *
                    this.windStrength * Math.pow(normalizedAge, 1.25);
            double e = Math.sin(Math.toRadians(this.particleRandom * 60.0F + this.windDirectionOffset)) *
                    this.windStrength * Math.pow(normalizedAge, 1.25);
            this.xd += d * 0.0025F;
            this.zd += e * 0.0025F;
            this.yd = this.yd - (double) this.gravity;
            if (!this.onGround) {
                this.rotSpeed = this.rotSpeed + this.spinAcceleration / 20.0F;
                this.oRoll = this.roll;
                this.roll = this.roll + this.rotSpeed / 20.0F;
            }
            this.move(this.xd, this.yd, this.zd);
            if (this.windStrength != 0.0 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove();
            }

            if (!this.removed) {
                this.xd = this.xd * (double) this.friction;
                this.yd = this.yd * (double) this.friction;
                this.zd = this.zd * (double) this.friction;
            }
        }
    }

    private void tickAge() {
        if (this.onGround) {
            switch (this.decayBehavior) {
                case FAST -> this.age += 5;
                case SLOW -> this.age++;
                case IMMEDIATE -> this.age = this.lifetime;
            }
        } else {
            this.age++;
        }
    }

    @Override
    public void move(double x, double y, double z) {
        // copied from super, but with custom collisions, which take visual shape into account
        if (!this.stoppedByCollision) {
            double d = x;
            double e = y;
            double f = z;
            if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) &&
                    x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
                Vec3 vec3 = ParticleCollisionHelper.collideBoundingBox(null,
                        new Vec3(x, y, z),
                        this.getBoundingBox(),
                        this.level,
                        List.of());
                x = vec3.x;
                y = vec3.y;
                z = vec3.z;
            }

            if (x != 0.0 || y != 0.0 || z != 0.0) {
                this.setBoundingBox(this.getBoundingBox().move(x, y, z));
                this.setLocationFromBoundingbox();
            }

            if (Math.abs(e) >= 1.0E-5F && Math.abs(y) < 1.0E-5F) {
                this.stoppedByCollision = true;
            }

            this.onGround = e != y && e < 0.0;
            if (d != x) {
                this.xd = 0.0;
            }

            if (f != z) {
                this.zd = 0.0;
            }
        }
    }

    @Nullable
    static TerrainParticle createTerrainParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        BlockState blockState = type.getState();
        return !blockState.isAir() && !blockState.is(Blocks.MOVING_PISTON) && blockState.shouldSpawnTerrainParticles() ?
                new FallingLeavesParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockState) : null;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<BlockParticleOption> {

        @Nullable
        public Particle createParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return createTerrainParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    public enum DecayBehavior {
        IMMEDIATE,
        FAST,
        SLOW
    }
}
