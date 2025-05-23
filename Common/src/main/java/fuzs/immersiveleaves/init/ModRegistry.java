package fuzs.immersiveleaves.init;

import com.mojang.serialization.MapCodec;
import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(ImmersiveLeaves.MOD_ID);
    public static final Holder.Reference<ParticleType<BlockParticleOption>> FALLING_LEAVES_PARTICLE_TYPE = registerParticleType(
            "falling_leaves",
            false,
            BlockParticleOption::codec,
            BlockParticleOption::streamCodec);

    public static void bootstrap() {
        // NO-OP
    }

    private static <T extends ParticleOptions> Holder.Reference<ParticleType<T>> registerParticleType(String name, boolean overrideLimiter, Function<ParticleType<T>, MapCodec<T>> codecGetter, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter) {
        return REGISTRIES.register(Registries.PARTICLE_TYPE, name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        });
    }
}
