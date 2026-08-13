package fuzs.fallingleavesplus.common.client.particle;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.client.particle.settings.AdditionalSettings;
import fuzs.fallingleavesplus.common.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.common.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.common.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.common.config.ClientConfig;
import fuzs.fallingleavesplus.common.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @see net.minecraft.client.particle.TerrainParticle
 */
public class TerrainFallingLeavesParticle extends CustomFallingLeavesParticle {
    private final float uo;
    private final float vo;

    public TerrainFallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, BlockState blockState, VanillaSettings vanillaSettings, AdditionalSettings additionalSettings) {
        super(level, x, y, z, sprite, vanillaSettings, additionalSettings);
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(blockState.getBlock())) {
            this.quadSize /= 2.0F;
            this.setSize(this.quadSize, this.quadSize);
        }

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
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
        public @Nullable Particle createParticle(FallingLeavesParticleOption particleOptions, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (!particleOptions.blockState().isAir() && particleOptions.blockState().shouldSpawnTerrainParticles()) {
                ParticleSettings particleSettings = FallingLeavesManager.getParticleSettings(particleOptions.blockState());
                SingleQuadParticle particle = new TerrainFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        AbstractFallingLeavesParticleProvider.getParticleTextureAtlas()
                                .getSprite(MissingTextureAtlasSprite.getLocation()),
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
