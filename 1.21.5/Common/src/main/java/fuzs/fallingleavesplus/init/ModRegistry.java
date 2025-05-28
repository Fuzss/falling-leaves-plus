package fuzs.fallingleavesplus.init;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(FallingLeavesPlus.MOD_ID);
    public static final Holder.Reference<ParticleType<BlockParticleOption>> FALLING_LEAVES_PARTICLE_TYPE = REGISTRIES.registerParticleType(
            "falling_leaves",
            false,
            BlockParticleOption::codec,
            BlockParticleOption::streamCodec);

    public static void bootstrap() {
        // NO-OP
    }
}
