package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.CustomFallingLeavesParticle;
import fuzs.fallingleavesplus.client.particle.TerrainFallingLeavesParticle;
import fuzs.fallingleavesplus.config.ClientConfig;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public record ParticleSettings(Either<Block, Optional<List<ResourceLocation>>> texture,
                               Optional<Boolean> tinted,
                               Optional<Float> leafParticleChance,
                               Optional<Boolean> spawnSnowFlakes,
                               Optional<BehavioralSettings> behavior,
                               Optional<EnvironmentalSettings> environment) {
    public static final Codec<ParticleSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.mapEither(
                            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block"),
                            ResourceLocation.CODEC.listOf().optionalFieldOf("textures")).forGetter(ParticleSettings::texture),
                    Codec.BOOL.lenientOptionalFieldOf("tinted").forGetter(ParticleSettings::tinted),
                    Codec.floatRange(0.0F, 1.0F)
                            .lenientOptionalFieldOf("leaf_particle_chance")
                            .forGetter(ParticleSettings::leafParticleChance),
                    Codec.BOOL.lenientOptionalFieldOf("spawn_snow_flakes").forGetter(ParticleSettings::spawnSnowFlakes),
                    BehavioralSettings.CODEC.lenientOptionalFieldOf("behavior").forGetter(ParticleSettings::behavior),
                    EnvironmentalSettings.CODEC.lenientOptionalFieldOf("environment").forGetter(ParticleSettings::environment))
            .apply(instance, ParticleSettings::new));
    public static final ParticleSettings DEFAULT = new ParticleSettings(Either.right(Optional.empty()),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
    static final List<ResourceLocation> DEFAULT_LEAVES_TEXTURES = IntStream.range(0, 2)
            .mapToObj((int i) -> ResourceLocationHelper.withDefaultNamespace("leaf_" + i))
            .toList();

    public float getLeafParticleChance() {
        return this.leafParticleChance.orElseGet(() -> (float) FallingLeavesPlus.CONFIG.get(ClientConfig.class).leafParticleChance);
    }

    public float getLeafParticleChanceWithWeather(Level level) {
        return this.getLeafParticleChance() *
                this.getEnvironment().getWeatherMultiplier(level.isRaining(), level.isThundering());
    }

    public List<ResourceLocation> getLeavesTextures() {
        return this.texture.right().flatMap(Function.identity()).orElse(DEFAULT_LEAVES_TEXTURES);
    }

    public boolean isTinted() {
        return this.tinted.orElse(Boolean.TRUE);
    }

    public boolean getSpawnSnowFlakes() {
        return this.spawnSnowFlakes.orElseGet(() -> FallingLeavesPlus.CONFIG.get(ClientConfig.class).spawnSnowFlakes);
    }

    public BehavioralSettings getBehavior() {
        return this.behavior.orElse(BehavioralSettings.DEFAULT);
    }

    public EnvironmentalSettings getEnvironment() {
        return this.environment.orElse(EnvironmentalSettings.DEFAULT);
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

    public static class ParticleProviderImpl implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public ParticleProviderImpl(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BlockParticleOption blockParticleOption, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (blockParticleOption.getState().shouldSpawnTerrainParticles()) {
                ParticleSettings particleSettings = FallingLeavesManager.getParticleSettings(blockParticleOption.getState());
                Optional<BlockState> optional = particleSettings.texture().left().map(Block::defaultBlockState);
                SpriteSet spriteSet = createSpriteSet(particleSettings.getLeavesTextures());
                TextureSheetParticle particle;
                if (optional.isPresent()) {
                    particle = new TerrainFallingLeavesParticle(clientLevel,
                            x,
                            y,
                            z,
                            spriteSet,
                            optional.get(),
                            particleSettings);
                } else {
                    particle = new CustomFallingLeavesParticle(clientLevel, x, y, z, spriteSet, particleSettings);
                }
                BlockPos blockPos = BlockPos.containing(x, y, z);
                BlockState blockState = clientLevel.getBlockState(blockPos.above());
                if (blockState.is(Blocks.SNOW)) {
                    particle.setSpriteFromAge(this.spriteSet);
                    particle.setColor(0.923F, 0.964F, 0.999F);
                } else if (particleSettings.isTinted()) {
                    int blockColor = Minecraft.getInstance()
                            .getBlockColors()
                            .getColor(blockParticleOption.getState(), clientLevel, blockPos, 0);
                    particle.rCol *= (blockColor >> 16 & 0xFF) / 255.0F;
                    particle.gCol *= (blockColor >> 8 & 0xFF) / 255.0F;
                    particle.bCol *= (blockColor & 0xFF) / 255.0F;
                }
                return particle;
            } else {
                return null;
            }
        }
    }
}
