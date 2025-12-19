package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.config.ClientConfig;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record VanillaSettings(Optional<Float> gravityMultiplier,
                              Optional<Float> windStrength,
                              Optional<Boolean> swirlAround,
                              Optional<Boolean> flowAway,
                              Optional<Float> leafSize,
                              Optional<Float> initialFallingSpeed) {
    public static final MapCodec<VanillaSettings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.floatRange(
                    0.0F,
                    Float.MAX_VALUE).lenientOptionalFieldOf("gravity_multiplier").forGetter(VanillaSettings::gravityMultiplier),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("wind_strength")
                    .forGetter(VanillaSettings::windStrength),
            Codec.BOOL.lenientOptionalFieldOf("swirl_around").forGetter(VanillaSettings::swirlAround),
            Codec.BOOL.lenientOptionalFieldOf("flow_away").forGetter(VanillaSettings::flowAway),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("leaf_size")
                    .forGetter(VanillaSettings::leafSize),
            Codec.floatRange(0.0F, Float.MAX_VALUE)
                    .lenientOptionalFieldOf("initial_falling_speed")
                    .forGetter(VanillaSettings::initialFallingSpeed)).apply(instance, VanillaSettings::new));
    public static final VanillaSettings DEFAULT = new VanillaSettings(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    public static VanillaSettings of(float gravityMultiplier, float windStrength, boolean swirlAround, boolean flowAway, float initialFallingSpeed) {
        return new VanillaSettings(Optional.of(gravityMultiplier),
                Optional.of(windStrength),
                Optional.of(swirlAround),
                Optional.of(flowAway),
                Optional.empty(),
                Optional.of(initialFallingSpeed));
    }

    public static VanillaSettings fixedSize(float leafSize) {
        return new VanillaSettings(Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(leafSize),
                Optional.empty());
    }

    public float getGravityMultiplier() {
        return this.gravityMultiplier.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.gravityMultiplier);
    }

    public float getWindStrength() {
        return this.windStrength.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.windStrength);
    }

    public float getWindStrength(Level level, AdditionalSettings additionalSettings) {
        return this.getWindStrength() * additionalSettings.getWeatherMultiplier(level.isRaining(),
                level.isThundering());
    }

    public boolean getSwirlAround() {
        return this.swirlAround.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.swirlAround);
    }

    public boolean getFlowAway() {
        return this.flowAway.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.flowAway);
    }

    public float getLeafSize() {
        return this.leafSize.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.leafSize);
    }

    public float getFallingSpeed() {
        return this.initialFallingSpeed.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).vanilla.initialFallingSpeed);
    }
}
