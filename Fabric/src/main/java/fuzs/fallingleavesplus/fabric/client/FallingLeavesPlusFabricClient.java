package fuzs.fallingleavesplus.fabric.client;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.FallingLeavesPlusClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class FallingLeavesPlusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(FallingLeavesPlus.MOD_ID, FallingLeavesPlusClient::new);
    }
}
