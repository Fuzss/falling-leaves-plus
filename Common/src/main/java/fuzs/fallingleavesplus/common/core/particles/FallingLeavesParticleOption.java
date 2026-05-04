package fuzs.fallingleavesplus.common.core.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record FallingLeavesParticleOption(ParticleType<FallingLeavesParticleOption> particleType,
                                          BlockState blockState,
                                          int tintColor) implements ParticleOptions {

    public FallingLeavesParticleOption(ParticleType<FallingLeavesParticleOption> particleType, BlockState blockState) {
        this(particleType, blockState, -1);
    }

    public static MapCodec<FallingLeavesParticleOption> codec(ParticleType<FallingLeavesParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(BlockState.CODEC.fieldOf("block_state")
                                .forGetter(FallingLeavesParticleOption::blockState),
                        ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("tint_color").forGetter(FallingLeavesParticleOption::tintColor))
                .apply(instance, (BlockState blockState, Integer color) -> {
                    return new FallingLeavesParticleOption(particleType, blockState, color);
                }));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, FallingLeavesParticleOption> streamCodec(ParticleType<FallingLeavesParticleOption> particleType) {
        return StreamCodec.composite(ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY),
                FallingLeavesParticleOption::blockState,
                ByteBufCodecs.INT,
                FallingLeavesParticleOption::tintColor,
                (BlockState blockState, Integer color) -> {
                    return new FallingLeavesParticleOption(particleType, blockState, color);
                });
    }

    @Override
    public ParticleType<?> getType() {
        return this.particleType;
    }

    public float getRed() {
        return ARGB.redFloat(this.tintColor);
    }

    public float getGreen() {
        return ARGB.greenFloat(this.tintColor);
    }

    public float getBlue() {
        return ARGB.blueFloat(this.tintColor);
    }
}
