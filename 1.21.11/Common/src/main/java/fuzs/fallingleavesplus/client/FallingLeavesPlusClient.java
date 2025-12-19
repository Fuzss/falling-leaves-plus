package fuzs.fallingleavesplus.client;

import fuzs.fallingleavesplus.client.particle.AbstractFallingLeavesParticleProvider;
import fuzs.fallingleavesplus.client.particle.TerrainFallingLeavesParticle;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ParticleProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ResourcePackReloadListenersContext;

public class FallingLeavesPlusClient implements ClientModConstructor {

    @Override
    public void onRegisterParticleProviders(ParticleProvidersContext context) {
        context.registerParticleProvider(ModRegistry.TERRAIN_LEAVES_PARTICLE_TYPE.value(),
                new TerrainFallingLeavesParticle.Provider());
        context.registerParticleProvider(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                new AbstractFallingLeavesParticleProvider.LeavesProvider());
        context.registerParticleProvider(ModRegistry.FALLING_SNOWFLAKE_PARTICLE_TYPE.value(),
                AbstractFallingLeavesParticleProvider.SnowflakeProvider::new);
    }

    @Override
    public void onAddResourcePackReloadListeners(ResourcePackReloadListenersContext context) {
        FallingLeavesManager.onAddResourcePackReloadListeners(context::registerReloadListener);
    }
}
