package fuzs.fallingleavesplus.core.particles;

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
                                          int color) implements ParticleOptions {

    public static MapCodec<FallingLeavesParticleOption> codec(ParticleType<FallingLeavesParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(BlockState.CODEC.fieldOf("block_state")
                                .forGetter(FallingLeavesParticleOption::blockState),
                        ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(FallingLeavesParticleOption::color))
                .apply(instance, (BlockState blockState, Integer color) -> {
                    return new FallingLeavesParticleOption(particleType, blockState, color);
                }));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, FallingLeavesParticleOption> streamCodec(ParticleType<FallingLeavesParticleOption> particleType) {
        return StreamCodec.composite(ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY),
                FallingLeavesParticleOption::blockState,
                ByteBufCodecs.INT,
                FallingLeavesParticleOption::color,
                (BlockState blockState, Integer color) -> {
                    return new FallingLeavesParticleOption(particleType, blockState, color);
                });
    }

    @Override
    public ParticleType<?> getType() {
        return this.particleType;
    }

    public float getRed() {
        return ARGB.red(this.color) / 255.0F;
    }

    public float getGreen() {
        return ARGB.green(this.color) / 255.0F;
    }

    public float getBlue() {
        return ARGB.blue(this.color) / 255.0F;
    }
}
