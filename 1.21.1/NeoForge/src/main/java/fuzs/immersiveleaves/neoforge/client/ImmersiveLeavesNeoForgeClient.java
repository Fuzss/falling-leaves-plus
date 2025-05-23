package fuzs.immersiveleaves.neoforge.client;

import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.immersiveleaves.client.ImmersiveLeavesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ImmersiveLeaves.MOD_ID, dist = Dist.CLIENT)
public class ImmersiveLeavesNeoForgeClient {

    public ImmersiveLeavesNeoForgeClient() {
        ClientModConstructor.construct(ImmersiveLeaves.MOD_ID, ImmersiveLeavesClient::new);
    }
}
