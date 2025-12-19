package fuzs.fallingleavesplus.neoforge;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(FallingLeavesPlus.MOD_ID)
public class FallingLeavesPlusNeoForge {

    public FallingLeavesPlusNeoForge() {
        ModConstructor.construct(FallingLeavesPlus.MOD_ID, FallingLeavesPlus::new);
    }
}
