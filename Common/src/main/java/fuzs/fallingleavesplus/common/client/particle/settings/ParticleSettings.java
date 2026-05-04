package fuzs.fallingleavesplus.common.client.particle.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.config.ClientConfig;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record ParticleSettings(List<ParticleTexture> textures,
                               Optional<Float> leafParticleChance,
                               Optional<Boolean> spawnSnowflakes,
                               VanillaSettings vanillaSettings,
                               AdditionalSettings additionalSettings) {
    public static final Codec<ParticleSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ParticleTexture.CODEC.listOf()
                            .lenientOptionalFieldOf("textures", ParticleTexture.DEFAULT)
                            .forGetter(ParticleSettings::textures),
                    Codec.floatRange(0.0F, 1.0F)
                            .lenientOptionalFieldOf("leaf_particle_chance")
                            .forGetter(ParticleSettings::leafParticleChance),
                    Codec.BOOL.lenientOptionalFieldOf("spawn_snowflakes").forGetter(ParticleSettings::spawnSnowflakes),
                    VanillaSettings.CODEC.forGetter(ParticleSettings::vanillaSettings),
                    AdditionalSettings.CODEC.forGetter(ParticleSettings::additionalSettings))
            .apply(instance, ParticleSettings::new));
    public static final ParticleSettings DEFAULT = builder().build();

    public float getLeafParticleChance() {
        return this.leafParticleChance.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).leafParticleChance);
    }

    public float getLeafParticleChanceWithWeather(Level level) {
        return this.getLeafParticleChance() * this.additionalSettings()
                .getWeatherMultiplier(level.isRaining(), level.isThundering());
    }

    public List<ParticleTexture> getLeavesTextures() {
        return this.textures;
    }

    public boolean getSpawnSnowflakes() {
        return this.spawnSnowflakes.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).spawnSnowflakes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ParticleSettings fromTextures(List<ParticleTexture> textures) {
        return builder().setTextures(textures).build();
    }

    public static class Builder {
        private List<ParticleTexture> textures = ParticleTexture.DEFAULT;
        private Optional<Float> leafParticleChance = Optional.empty();
        private Optional<Boolean> spawnSnowflakes = Optional.empty();
        private VanillaSettings vanillaSettings = VanillaSettings.DEFAULT;
        private AdditionalSettings additionalSettings = AdditionalSettings.DEFAULT;

        public Builder setTextures(List<ParticleTexture> textures) {
            Objects.requireNonNull(textures, "textures is null");
            this.textures = textures;
            return this;
        }

        public Builder setLeafParticleChance(float leafParticleChance) {
            this.leafParticleChance = Optional.of(leafParticleChance);
            return this;
        }

        public Builder setSpawnSnowflakes(boolean spawnSnowflakes) {
            this.spawnSnowflakes = Optional.of(spawnSnowflakes);
            return this;
        }

        public Builder setVanillaSettings(VanillaSettings vanillaSettings) {
            this.vanillaSettings = vanillaSettings;
            return this;
        }

        public Builder setAdditionalSettings(AdditionalSettings additionalSettings) {
            this.additionalSettings = additionalSettings;
            return this;
        }

        public ParticleSettings build() {
            return new ParticleSettings(this.textures,
                    this.leafParticleChance,
                    this.spawnSnowflakes,
                    this.vanillaSettings,
                    this.additionalSettings);
        }
    }
}
