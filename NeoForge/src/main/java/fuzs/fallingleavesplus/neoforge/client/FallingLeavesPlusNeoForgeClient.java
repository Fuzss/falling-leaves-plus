package fuzs.fallingleavesplus.neoforge.client;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.client.FallingLeavesPlusClient;
import fuzs.fallingleavesplus.common.data.client.ModParticleProvider;
import fuzs.fallingleavesplus.common.data.client.ModParticleSettingsProvider;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
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
