package fuzs.fallingleavesplus.init;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.core.particles.FallingLeavesParticleOption;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(FallingLeavesPlus.MOD_ID);
    public static final Holder.Reference<ParticleType<FallingLeavesParticleOption>> FALLING_LEAVES_PARTICLE_TYPE = REGISTRIES.registerParticleType(
            "falling_leaves",
            false,
            FallingLeavesParticleOption::codec,
            FallingLeavesParticleOption::streamCodec);

    public static void bootstrap() {
        // NO-OP
    }
}
