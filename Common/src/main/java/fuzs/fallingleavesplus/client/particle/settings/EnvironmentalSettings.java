package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.config.ClientConfig;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record EnvironmentalSettings(Optional<Float> rainAmplifier,
                                    Optional<Float> thunderstormAmplifier,
                                    Optional<Float> windStrength,
                                    Optional<Float> windDirection) {
    public static final Codec<EnvironmentalSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(
                    1.0F,
                    Float.MAX_VALUE).lenientOptionalFieldOf("rain_amplifier").forGetter(EnvironmentalSettings::rainAmplifier),
            Codec.floatRange(1.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("thunderstorm_amplifier")
                    .forGetter(EnvironmentalSettings::thunderstormAmplifier),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("wind_strength")
                    .forGetter(EnvironmentalSettings::windStrength),
            Codec.floatRange(0.0F, 360.0F)
                    .lenientOptionalFieldOf("wind_direction")
                    .forGetter(EnvironmentalSettings::windDirection)).apply(instance, EnvironmentalSettings::new));
    public static final EnvironmentalSettings DEFAULT = new EnvironmentalSettings(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    public float getRainAmplifier() {
        return this.rainAmplifier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).environment.rainAmplifier);
    }

    public float getThunderstormAmplifier() {
        return this.thunderstormAmplifier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).environment.thunderstormAmplifier);
    }

    public float getWindStrength() {
        return this.windStrength.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).environment.windStrength);
    }

    public float getWindDirection() {
        return this.windDirection.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).environment.windDirection);
    }

    public float getWindStrength(Level level) {
        return this.getWindStrength() * this.getWeatherMultiplier(level.isRaining(), level.isThundering());
    }

    public float getWeatherMultiplier(boolean isRaining, boolean isThundering) {
        return (isRaining ? this.getRainAmplifier() : 1.0F) * (isThundering ? this.getThunderstormAmplifier() : 1.0F);
    }
}
