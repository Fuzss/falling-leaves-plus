package fuzs.fallingleavesplus.common.data.client;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.client.data.v3.particles.AbstractParticleProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;

public class ModParticleProvider extends AbstractParticleProvider {

    public ModParticleProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticles() {
        this.add(ModRegistry.FALLING_SNOWFLAKE_PARTICLE_TYPE.value(), FallingLeavesPlus.id("snowflake"), 3, 0);
    }
}
