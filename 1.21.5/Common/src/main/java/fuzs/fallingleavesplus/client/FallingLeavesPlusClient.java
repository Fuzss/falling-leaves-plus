package fuzs.fallingleavesplus.client;

import fuzs.fallingleavesplus.client.particle.FallingLeavesParticleProvider;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ParticleProvidersContext;
import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;

public class FallingLeavesPlusClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        AddResourcePackReloadListenersCallback.EVENT.register(FallingLeavesManager::onAddResourcePackReloadListeners);
    }

    @Override
    public void onRegisterParticleProviders(ParticleProvidersContext context) {
        context.registerParticleProvider(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                FallingLeavesParticleProvider::new);
    }
}
