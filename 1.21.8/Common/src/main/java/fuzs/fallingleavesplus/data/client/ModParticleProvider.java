package fuzs.fallingleavesplus.data.client;

import fuzs.fallingleavesplus.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractParticleProvider;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModParticleProvider extends AbstractParticleProvider {

    public ModParticleProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticles() {
        // use snowflake particles for the sprite, so we can easily use that when necessary
        // normally sprites are set to the correct leaf texture on the fly
        this.add(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                ResourceLocationHelper.withDefaultNamespace("generic"),
                7,
                0);
    }
}
