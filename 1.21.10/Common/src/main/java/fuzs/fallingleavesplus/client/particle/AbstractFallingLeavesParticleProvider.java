package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.client.particle.settings.ParticleTexture;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AbstractFallingLeavesParticleProvider<T> implements ParticleProvider<FallingLeavesParticleOption> {

    @Override
    public Particle createParticle(FallingLeavesParticleOption particleOptions, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
        ParticleSettings particleSettings = this.getParticleSettings(particleOptions.blockState());
        T particleTexture = this.pickParticleTexture(particleSettings, randomSource);
        TextureAtlasSprite textureAtlasSprite = this.pickSprite(particleTexture, randomSource);
        CustomFallingLeavesParticle particle = new CustomFallingLeavesParticle(clientLevel,
                x,
                y,
                z,
                textureAtlasSprite,
                particleSettings.vanillaSettings(),
                particleSettings.additionalSettings());
        this.setTintColor(particle, particleOptions.tintColor(), particleTexture);
        this.setQuadSize(particle, particleTexture);
        return particle;
    }

    protected abstract ParticleSettings getParticleSettings(BlockState blockState);

    protected abstract T pickParticleTexture(ParticleSettings particleSettings, RandomSource randomSource);

    protected abstract TextureAtlasSprite pickSprite(T particleTexture, RandomSource randomSource);

    protected abstract void setTintColor(CustomFallingLeavesParticle particle, int tintColor, T particleTexture);

    protected abstract void setQuadSize(CustomFallingLeavesParticle particle, T particleTexture);

    public static TextureAtlas getParticleTextureAtlas() {
        return (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES);
    }

    public static class LeavesProvider extends AbstractFallingLeavesParticleProvider<ParticleTexture> {
        @Override
        protected ParticleSettings getParticleSettings(BlockState blockState) {
            return FallingLeavesManager.getParticleSettings(blockState);
        }

        @Override
        protected @Nullable ParticleTexture pickParticleTexture(ParticleSettings particleSettings, RandomSource randomSource) {
            List<ParticleTexture> textures = particleSettings.getLeavesTextures();
            return textures.get(randomSource.nextInt(textures.size()));
        }

        @Override
        protected TextureAtlasSprite pickSprite(@Nullable ParticleTexture particleTexture, RandomSource randomSource) {
            Objects.requireNonNull(particleTexture, "particle texture is null");
            return getParticleTextureAtlas().getSprite(particleTexture.id());
        }

        @Override
        protected void setTintColor(CustomFallingLeavesParticle particle, int tintColor, @Nullable ParticleTexture particleTexture) {
            Objects.requireNonNull(particleTexture, "particle texture is null");
            if (particleTexture.isTinted()) {
                tintColor = particleTexture.pickTintColor(tintColor);
                particle.rCol *= ARGB.redFloat(tintColor);
                particle.gCol *= ARGB.greenFloat(tintColor);
                particle.bCol *= ARGB.blueFloat(tintColor);
            }
        }

        @Override
        protected void setQuadSize(CustomFallingLeavesParticle particle, ParticleTexture particleTexture) {
            if (particleTexture.textureScale() != 1.0F) {
                particle.scaleSize(particleTexture.textureScale());
            }
        }
    }

    public static class SnowflakeProvider extends AbstractFallingLeavesParticleProvider<Unit> {
        private static final ParticleSettings PARTICLE_SETTINGS = ParticleSettings.builder()
                .setVanillaSettings(VanillaSettings.fixedSize(2.0F))
                .build();

        private final SpriteSet sprites;

        public SnowflakeProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        protected ParticleSettings getParticleSettings(BlockState blockState) {
            return PARTICLE_SETTINGS;
        }

        @Override
        protected Unit pickParticleTexture(ParticleSettings particleSettings, RandomSource randomSource) {
            return Unit.INSTANCE;
        }

        @Override
        protected TextureAtlasSprite pickSprite(Unit particleTexture, RandomSource randomSource) {
            return this.sprites.get(randomSource);
        }

        /**
         * @see net.minecraft.client.particle.SnowflakeParticle.Provider#createParticle(SimpleParticleType,
         *         ClientLevel, double, double, double, double, double, double, RandomSource)
         */
        @Override
        protected void setTintColor(CustomFallingLeavesParticle particle, int tintColor, Unit particleTexture) {
            particle.setColor(0.923F, 0.964F, 0.999F);
        }

        @Override
        protected void setQuadSize(CustomFallingLeavesParticle particle, Unit particleTexture) {
            // NO-OP
        }
    }
}
