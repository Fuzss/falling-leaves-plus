package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.config.ClientConfig;
import fuzs.fallingleavesplus.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public record FallingLeavesParticleProvider(SpriteSet sprites) implements ParticleProvider<FallingLeavesParticleOption> {

    @Nullable
    @Override
    public Particle createParticle(FallingLeavesParticleOption particleOptions, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
        if (particleOptions.blockState().shouldSpawnTerrainParticles()) {
            ParticleSettings particleSettings = FallingLeavesManager.getParticleSettings(particleOptions.blockState());
            boolean isSnowyBlock = FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(
                    particleOptions.blockState().getBlock());
            SingleQuadParticle particle;
            if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).blockParticles) {
                particle = new TerrainFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        ParticleSettings.createDefaultSpriteSet().get(randomSource),
                        particleOptions.blockState(),
                        particleSettings.vanillaSettings(),
                        particleSettings.additionalSettings());
                if (!isSnowyBlock) {
                    particle.rCol *= particleOptions.getRed();
                    particle.gCol *= particleOptions.getGreen();
                    particle.bCol *= particleOptions.getBlue();
                }
            } else {
                // don't advance sprite based on age for snow, just pick a random one
                SpriteSet sprites = isSnowyBlock ? this.sprites :
                        ParticleSettings.createSpriteSet(particleSettings.getLeavesTextures());
                particle = new CustomFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        sprites.get(randomSource),
                        particleSettings.vanillaSettings(),
                        particleSettings.additionalSettings());
                if (isSnowyBlock) {
                    // copied from snowflake particle
                    particle.setColor(0.923F, 0.964F, 0.999F);
                } else if (particleSettings.isTinted()) {
                    int tintColor = particleSettings.pickTintColor(particleOptions.color());
                    particle.rCol *= ARGB.redFloat(tintColor);
                    particle.gCol *= ARGB.greenFloat(tintColor);
                    particle.bCol *= ARGB.blueFloat(tintColor);
                }
            }

            return particle;
        } else {
            return null;
        }
    }
}
