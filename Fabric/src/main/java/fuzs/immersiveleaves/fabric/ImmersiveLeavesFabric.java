package fuzs.immersiveleaves.fabric;

import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class ImmersiveLeavesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(ImmersiveLeaves.MOD_ID, ImmersiveLeaves::new);
    }
}
