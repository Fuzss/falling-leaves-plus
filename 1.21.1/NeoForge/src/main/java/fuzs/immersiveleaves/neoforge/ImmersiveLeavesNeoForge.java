package fuzs.immersiveleaves.neoforge;

import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(ImmersiveLeaves.MOD_ID)
public class ImmersiveLeavesNeoForge {

    public ImmersiveLeavesNeoForge() {
        ModConstructor.construct(ImmersiveLeaves.MOD_ID, ImmersiveLeaves::new);
    }
}
