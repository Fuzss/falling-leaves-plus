package fuzs.fallingleavesplus.neoforge.data.client;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractParticleProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;

public class ModParticleProvider extends AbstractParticleProvider {

    public ModParticleProvider(NeoForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticles() {
        this.add(FallingLeavesPlus.id("birch_leaves"), ResourceLocationHelper.withDefaultNamespace("leaf"), 11);
        this.add(FallingLeavesPlus.id("spruce_leaves"), ResourceLocationHelper.withDefaultNamespace("leaf"), 11);
        this.add(FallingLeavesPlus.id("acacia_leaves"), ResourceLocationHelper.withDefaultNamespace("leaf"), 11);
    }
}
