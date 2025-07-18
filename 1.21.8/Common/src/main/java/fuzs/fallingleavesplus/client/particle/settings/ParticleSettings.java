package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.config.ClientConfig;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public record ParticleSettings(Optional<List<ResourceLocation>> textures,
                               Either<Integer, Optional<Boolean>> tint,
                               Optional<Float> leafParticleChance,
                               Optional<Boolean> spawnSnowflakes,
                               VanillaSettings vanillaSettings,
                               AdditionalSettings additionalSettings) {
    public static final Codec<ParticleSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.listOf().optionalFieldOf("textures").forGetter(ParticleSettings::textures),
                    Codec.mapEither(ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("tint_color"),
                            Codec.BOOL.lenientOptionalFieldOf("tinted")).forGetter(ParticleSettings::tint),
                    Codec.floatRange(0.0F, 1.0F)
                            .lenientOptionalFieldOf("leaf_particle_chance")
                            .forGetter(ParticleSettings::leafParticleChance),
                    Codec.BOOL.lenientOptionalFieldOf("spawn_snowflakes").forGetter(ParticleSettings::spawnSnowflakes),
                    VanillaSettings.CODEC.forGetter(ParticleSettings::vanillaSettings),
                    AdditionalSettings.CODEC.forGetter(ParticleSettings::additionalSettings))
            .apply(instance, ParticleSettings::new));
    public static final ParticleSettings DEFAULT = new ParticleSettings(Optional.empty(),
            Either.right(Optional.empty()),
            Optional.empty(),
            Optional.empty());
    static final List<ResourceLocation> DEFAULT_LEAVES_TEXTURES = createTextureLocations(ResourceLocationHelper.withDefaultNamespace(
            "leaf"));

    public ParticleSettings(List<ResourceLocation> textures, float leafParticleChance, VanillaSettings vanillaSettings) {
        this(Optional.of(textures),
                Either.right(Optional.of(Boolean.FALSE)),
                Optional.of(leafParticleChance),
                Optional.of(Boolean.TRUE),
                vanillaSettings,
                AdditionalSettings.DEFAULT);
    }

    public ParticleSettings(Optional<List<ResourceLocation>> textures, Either<Integer, Optional<Boolean>> tint, Optional<Float> leafParticleChance, Optional<Boolean> spawnSnowFlakes) {
        this(textures, tint, leafParticleChance, spawnSnowFlakes, VanillaSettings.DEFAULT, AdditionalSettings.DEFAULT);
    }

    public float getLeafParticleChance() {
        return this.leafParticleChance.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).leafParticleChance);
    }

    public float getLeafParticleChanceWithWeather(Level level) {
        return this.getLeafParticleChance() *
                this.additionalSettings().getWeatherMultiplier(level.isRaining(), level.isThundering());
    }

    public List<ResourceLocation> getLeavesTextures() {
        return this.textures.orElse(DEFAULT_LEAVES_TEXTURES);
    }

    public boolean isTinted() {
        return this.tint.right().flatMap(Function.identity()).orElse(Boolean.TRUE);
    }

    public int pickTintColor(int tintColor) {
        return this.tint.left().orElse(tintColor);
    }

    public boolean getSpawnSnowflakes() {
        return this.spawnSnowflakes.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).spawnSnowflakes);
    }

    public static SpriteSet createDefaultSpriteSet() {
        return createSpriteSet(DEFAULT_LEAVES_TEXTURES);
    }

    public static SpriteSet createSpriteSet(List<ResourceLocation> textureLocations) {
        ParticleEngine.MutableSpriteSet spriteSet = new ParticleEngine.MutableSpriteSet();
        List<TextureAtlasSprite> list = new ArrayList<>(textureLocations.size());
        TextureAtlas textureAtlas = (TextureAtlas) Minecraft.getInstance()
                .getTextureManager()
                .getTexture(TextureAtlas.LOCATION_PARTICLES);
        spriteSet.rebind(textureLocations.stream().map(textureAtlas::getSprite).toList());
        return spriteSet;
    }

    public static List<ResourceLocation> createTextureLocations(ResourceLocation resourceLocation) {
        return IntStream.range(0, 12)
                .mapToObj((int i) -> resourceLocation.withPath((String s) -> s + "_" + i))
                .toList();
    }
}
