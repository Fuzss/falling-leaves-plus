package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.client.particle.settings.AdditionalSettings;
import fuzs.fallingleavesplus.client.particle.settings.DecayMode;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.client.world.phys.shapes.ParticleCollisionHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CustomFallingLeavesParticle extends FallingLeavesParticle {
    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(1.0F, 0.0F, 0.95F, 1.0F);
    private final DecayMode decayMode;
    public final boolean collideWithVisualShape;

    public CustomFallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, VanillaSettings vanillaSettings, AdditionalSettings additionalSettings) {
        super(level,
                x,
                y,
                z,
                sprite,
                vanillaSettings.getGravityMultiplier(),
                vanillaSettings.getWindStrength(level, additionalSettings),
                vanillaSettings.getSwirlAround(),
                vanillaSettings.getFlowAway(),
                vanillaSettings.getLeafSize(),
                vanillaSettings.getFallingSpeed());
        this.lifetime = additionalSettings.getLifetimeInSeconds() * 20;
        float particleRandom = this.random.nextFloat();
        this.xaFlowScale =
                Math.cos(Math.toRadians(particleRandom * 60.0F + additionalSettings.getWindDirection())) * this.windBig;
        this.zaFlowScale =
                Math.sin(Math.toRadians(particleRandom * 60.0F + additionalSettings.getWindDirection())) * this.windBig;
        this.collideWithVisualShape = additionalSettings.getCollideWithVisualShapes();
        this.decayMode = additionalSettings.getDecayOnGroundMode();
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public void scaleSize(float scaleAmount) {
        this.quadSize *= scaleAmount;
        this.setSize(this.quadSize, this.quadSize);
    }

    @Override
    public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTick) {
        this.setAlpha(this.lifetimeAlpha.currentAlphaForAge(this.age, this.lifetime, partialTick));
        super.extract(reusedState, camera, partialTick);
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
            float g = this.getNormalizedAge();
            double d = 0.0;
            double e = 0.0;
            if (this.flowAway) {
                d += this.xaFlowScale * Math.pow(g, 1.25);
                e += this.zaFlowScale * Math.pow(g, 1.25);
            }

            if (this.swirl) {
                d += g * Math.cos(g * this.swirlPeriod) * this.windBig;
                e += g * Math.sin(g * this.swirlPeriod) * this.windBig;
            }

            this.xd += d * 0.0025F;
            this.zd += e * 0.0025F;
            this.yd = this.yd - (double) this.gravity;
            if (!this.onGround) {
                this.rotSpeed = this.rotSpeed + this.spinAcceleration / 20.0F;
                this.oRoll = this.roll;
                this.roll = this.roll + this.rotSpeed / 20.0F;
            }
            this.move(this.xd, this.yd, this.zd);
            if (this.windBig != 0.0 && (this.xd == 0.0 || this.zd == 0.0)) {
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
            switch (this.decayMode) {
                case FAST -> this.age += 5;
                case SLOW -> this.age++;
                case IMMEDIATE -> this.age = this.lifetime;
            }
        } else {
            this.age++;
        }
    }

    private float getNormalizedAge() {
        return Mth.clamp(this.age / (float) this.lifetime, 0.0F, 1.0F);
    }

    @Override
    public void move(double x, double y, double z) {
        // copied from super, but with custom collisions, which take visual shape into account
        if (this.collideWithVisualShape) {
            if (!this.stoppedByCollision) {
                double d = x;
                double e = y;
                double f = z;
                if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0)
                        && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
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
        } else {
            super.move(x, y, z);
        }
    }
}
