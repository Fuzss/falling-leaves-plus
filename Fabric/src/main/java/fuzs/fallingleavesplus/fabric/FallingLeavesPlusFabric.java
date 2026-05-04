package fuzs.fallingleavesplus.fabric;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class FallingLeavesPlusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(FallingLeavesPlus.MOD_ID, FallingLeavesPlus::new);
    }
}
