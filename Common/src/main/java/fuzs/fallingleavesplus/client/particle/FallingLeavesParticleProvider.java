package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.config.ClientConfig;
import fuzs.fallingleavesplus.core.particles.FallingLeavesParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;

public record FallingLeavesParticleProvider(SpriteSet spriteSet) implements ParticleProvider<FallingLeavesParticleOption> {

    @Nullable
    @Override
    public Particle createParticle(FallingLeavesParticleOption particleOptions, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if (particleOptions.blockState().shouldSpawnTerrainParticles()) {
            ParticleSettings particleSettings = FallingLeavesManager.getParticleSettings(particleOptions.blockState());
            boolean isSnowyBlock = FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(
                    particleOptions.blockState().getBlock());
            TextureSheetParticle particle;
            if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).blockParticles) {
                particle = new TerrainFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        particleOptions.blockState(),
                        particleSettings.vanillaSettings(),
                        particleSettings.additionalSettings());
                if (!isSnowyBlock) {
                    particle.rCol *= particleOptions.getRed();
                    particle.gCol *= particleOptions.getGreen();
                    particle.bCol *= particleOptions.getBlue();
                }
            } else {
                particle = new CustomFallingLeavesParticle(clientLevel,
                        x,
                        y,
                        z,
                        ParticleSettings.createSpriteSet(particleSettings.getLeavesTextures()),
                        particleSettings.vanillaSettings(),
                        particleSettings.additionalSettings());
                if (isSnowyBlock) {
                    // don't advance sprite based on age, just pick a random one
                    particle.pickSprite(this.spriteSet);
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
