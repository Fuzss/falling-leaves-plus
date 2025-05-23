package fuzs.immersiveleaves.fabric.client;

import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.immersiveleaves.client.ImmersiveLeavesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class ImmersiveLeavesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(ImmersiveLeaves.MOD_ID, ImmersiveLeavesClient::new);
    }
}
