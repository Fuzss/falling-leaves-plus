package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.AdditionalSettings;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.config.ClientConfig;
import fuzs.fallingleavesplus.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * @see net.minecraft.client.particle.TerrainParticle
 */
public class TerrainFallingLeavesParticle extends CustomFallingLeavesParticle {
    private final SingleQuadParticle.Layer layer;
    private final float uo;
    private final float vo;

    public TerrainFallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, BlockState blockState, VanillaSettings vanillaSettings, AdditionalSettings additionalSettings) {
        super(level, x, y, z, sprite, vanillaSettings, additionalSettings);
        Material.Baked particleMaterial = Minecraft.getInstance()
                .getModelManager()
                .getBlockStateModelSet()
                .getParticleMaterial(blockState);
        this.setSprite(particleMaterial.sprite());
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(blockState.getBlock())) {
            this.quadSize /= 2.0F;
            this.setSize(this.quadSize, this.quadSize);
        }

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    public static class Provider implements ParticleProvider<FallingLeavesParticleOption> {
        @Override
        public @Nullable Particle createParticle(FallingLeavesParticleOption particleOptions, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            if (!particleOptions.blockState().isAir() && particleOptions.blockState().shouldSpawnTerrainParticles()) {
                ParticleSettings particleSettings = FallingLeavesManager.getParticleSettings(particleOptions.blockState());
                SingleQuadParticle particle = new TerrainFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        AbstractFallingLeavesParticleProvider.getParticleTextureAtlas().missingSprite(),
                        particleOptions.blockState(),
                        particleSettings.vanillaSettings(),
                        particleSettings.additionalSettings());
                particle.rCol *= particleOptions.getRed();
                particle.gCol *= particleOptions.getGreen();
                particle.bCol *= particleOptions.getBlue();
                return particle;
            } else {
                return null;
            }
        }
    }
}
