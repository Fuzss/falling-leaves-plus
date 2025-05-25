package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.config.ClientConfig;

import java.util.Optional;

public record BehavioralSettings(Optional<Float> gravityMultiplier,
                                 Optional<Boolean> swirlAround,
                                 Optional<Boolean> flowAway,
                                 Optional<Float> leafSize,
                                 Optional<Float> fallingSpeed,
                                 Optional<Integer> lifetimeInSeconds,
                                 Optional<Boolean> collideWithVisualShapes,
                                 Optional<DecayMode> decayOnGroundMode) {
    public static final Codec<BehavioralSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(
                            0.0F,
                            Float.MAX_VALUE)
                    .lenientOptionalFieldOf("gravity_multiplier")
                    .forGetter(BehavioralSettings::gravityMultiplier),
            Codec.BOOL.lenientOptionalFieldOf("swirl_around").forGetter(BehavioralSettings::swirlAround),
            Codec.BOOL.lenientOptionalFieldOf("flow_away").forGetter(BehavioralSettings::flowAway),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("leaf_size")
                    .forGetter(BehavioralSettings::leafSize),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("falling_speed")
                    .forGetter(BehavioralSettings::fallingSpeed),
            Codec.intRange(0, Integer.MAX_VALUE)
                    .lenientOptionalFieldOf("lifetime_in_seconds")
                    .forGetter(BehavioralSettings::lifetimeInSeconds),
            Codec.BOOL.lenientOptionalFieldOf("collide_with_visual_shapes")
                    .forGetter(BehavioralSettings::collideWithVisualShapes),
            DecayMode.CODEC.lenientOptionalFieldOf("decay_on_ground_mode")
                    .forGetter(BehavioralSettings::decayOnGroundMode)).apply(instance, BehavioralSettings::new));
    public static final BehavioralSettings DEFAULT = new BehavioralSettings(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    public float getGravityMultiplier() {
        return this.gravityMultiplier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.gravityMultiplier);
    }

    public boolean getSwirlAround() {
        return this.swirlAround.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.swirlAround);
    }

    public boolean getFlowAway() {
        return this.flowAway.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.flowAway);
    }

    public float getLeafSize() {
        return this.leafSize.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.leafSize);
    }

    public float getFallingSpeed() {
        return this.fallingSpeed.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.fallingSpeed);
    }

    public int getLifetimeInSeconds() {
        return this.lifetimeInSeconds.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.lifetimeInSeconds);
    }

    public boolean getCollideWithVisualShapes() {
        return this.collideWithVisualShapes.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.collideWithVisualShapes);
    }

    public DecayMode getDecayOnGroundMode() {
        return this.decayOnGroundMode.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).behavior.decayOnGroundMode);
    }
}