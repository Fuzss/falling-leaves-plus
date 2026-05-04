package fuzs.fallingleavesplus.common.client.particle;

import fuzs.fallingleavesplus.common.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.common.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.common.client.particle.settings.ParticleTexture;
import fuzs.fallingleavesplus.common.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.common.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

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
        protected ParticleTexture pickParticleTexture(ParticleSettings particleSettings, RandomSource randomSource) {
            List<ParticleTexture> textures = particleSettings.getLeavesTextures();
            return textures.get(randomSource.nextInt(textures.size()));
        }

        @Override
        protected TextureAtlasSprite pickSprite(ParticleTexture particleTexture, RandomSource randomSource) {
            return getParticleTextureAtlas().getSprite(particleTexture.id());
        }

        @Override
        protected void setTintColor(CustomFallingLeavesParticle particle, int tintColor, ParticleTexture particleTexture) {
            if (particleTexture.isTinted()) {
                tintColor = particleTexture.pickTintColor(tintColor);
                particle.rCol *= ARGB.redFloat(tintColor);
                particle.gCol *= ARGB.greenFloat(tintColor);
                particle.bCol *= ARGB.blueFloat(tintColor);
            }
        }

        @Override
        protected void setQuadSize(CustomFallingLeavesParticle particle, ParticleTexture particleTexture) {
            particleTexture.textureScale().ifPresent(particle::scaleSize);
        }
    }

    public static class SnowflakeProvider extends AbstractFallingLeavesParticleProvider<Unit> {
        private static final ParticleSettings PARTICLE_SETTINGS = ParticleSettings.builder()
                .setVanillaSettings(VanillaSettings.fixedSize(2.4F))
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

        @Override
        protected void setTintColor(CustomFallingLeavesParticle particle, int tintColor, Unit particleTexture) {
            // NO-OP
        }

        @Override
        protected void setQuadSize(CustomFallingLeavesParticle particle, Unit particleTexture) {
            // NO-OP
        }
    }
}
