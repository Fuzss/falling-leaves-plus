package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.config.ClientConfig;

import java.util.Optional;

public record AdditionalSettings(Optional<Integer> lifetimeInSeconds,
                                 Optional<Boolean> collideWithVisualShapes,
                                 Optional<DecayMode> decayOnGroundMode,
                                 Optional<Float> windDirection,
                                 Optional<Float> rainAmplifier,
                                 Optional<Float> thunderstormAmplifier) {
    public static final MapCodec<AdditionalSettings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(0, Integer.MAX_VALUE)
                    .lenientOptionalFieldOf("lifetime_in_seconds")
                    .forGetter(AdditionalSettings::lifetimeInSeconds),
            Codec.BOOL.lenientOptionalFieldOf("collide_with_visual_shapes")
                    .forGetter(AdditionalSettings::collideWithVisualShapes),
            DecayMode.CODEC.lenientOptionalFieldOf("decay_on_ground_mode")
                    .forGetter(AdditionalSettings::decayOnGroundMode),
            Codec.floatRange(0.0F, 360.0F)
                    .lenientOptionalFieldOf("wind_direction")
                    .forGetter(AdditionalSettings::windDirection),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("rain_amplifier")
                    .forGetter(AdditionalSettings::rainAmplifier),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("thunderstorm_amplifier")
                    .forGetter(AdditionalSettings::thunderstormAmplifier)).apply(instance, AdditionalSettings::new));
    public static final AdditionalSettings DEFAULT = new AdditionalSettings(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    public static AdditionalSettings fixedLifetime(int lifetimeInSeconds) {
        return new AdditionalSettings(Optional.of(lifetimeInSeconds),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    public int getLifetimeInSeconds() {
        return this.lifetimeInSeconds.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.lifetimeInSeconds);
    }

    public boolean getCollideWithVisualShapes() {
        return this.collideWithVisualShapes.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.collideWithVisualShapes);
    }

    public DecayMode getDecayOnGroundMode() {
        return this.decayOnGroundMode.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.decayOnGroundMode);
    }

    public float getWindDirection() {
        return this.windDirection.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.windDirection);
    }

    public float getRainAmplifier() {
        return this.rainAmplifier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.rainAmplifier);
    }

    public float getThunderstormAmplifier() {
        return this.thunderstormAmplifier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).additional.thunderstormAmplifier);
    }

    public float getWeatherMultiplier(boolean isRaining, boolean isThundering) {
        return (isRaining ? this.getRainAmplifier() : 1.0F) * (isThundering ? this.getThunderstormAmplifier() : 1.0F);
    }
}
