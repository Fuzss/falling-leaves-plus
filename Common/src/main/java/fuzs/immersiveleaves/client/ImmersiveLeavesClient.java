package fuzs.immersiveleaves.client;

import fuzs.immersiveleaves.client.particle.FallingLeavesParticle;
import fuzs.immersiveleaves.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ParticleProvidersContext;

public class ImmersiveLeavesClient implements ClientModConstructor {

    @Override
    public void onRegisterParticleProviders(ParticleProvidersContext context) {
        context.registerParticleProvider(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                new FallingLeavesParticle.Provider());
    }
}
