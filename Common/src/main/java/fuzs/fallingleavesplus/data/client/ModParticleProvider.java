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
        this.add(ModRegistry.FALLING_SNOWFLAKE_PARTICLE_TYPE.value(),
                ResourceLocationHelper.withDefaultNamespace("generic"),
                7,
                0);
    }
}
