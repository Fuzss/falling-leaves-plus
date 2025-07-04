package fuzs.fallingleavesplus.neoforge.client;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.FallingLeavesPlusClient;
import fuzs.fallingleavesplus.data.client.ModParticleProvider;
import fuzs.fallingleavesplus.data.client.ModParticleSettingsProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = FallingLeavesPlus.MOD_ID, dist = Dist.CLIENT)
public class FallingLeavesPlusNeoForgeClient {

    public FallingLeavesPlusNeoForgeClient() {
        ClientModConstructor.construct(FallingLeavesPlus.MOD_ID, FallingLeavesPlusClient::new);
        DataProviderHelper.registerDataProviders(FallingLeavesPlus.MOD_ID,
                ModParticleProvider::new,
                ModParticleSettingsProvider::new);
    }
}
