package fuzs.fallingleavesplus.fabric;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class FallingLeavesPlusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(FallingLeavesPlus.MOD_ID, FallingLeavesPlus::new);
    }
}
