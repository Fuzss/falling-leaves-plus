package fuzs.immersiveleaves.mixin.client;

import fuzs.immersiveleaves.ImmersiveLeaves;
import fuzs.immersiveleaves.config.ClientConfig;
import fuzs.immersiveleaves.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo callback) {
        boolean raining = level.isRaining();
        boolean thundering = level.isThundering();
        int leafParticleChance;
        if (ImmersiveLeaves.CONFIG.get(ClientConfig.class).fallingLeaves.weatherEffects && (raining || thundering)) {
            leafParticleChance = raining && thundering ? 5 : 10;
        } else {
            leafParticleChance = 5;
        }
        if (random.nextInt(leafParticleChance) == 0) {
            BlockPos blockPos = pos.below();
            BlockState blockState = level.getBlockState(blockPos);
            if (!isFaceFull(blockState.getCollisionShape(level, blockPos), Direction.UP)) {
                BlockParticleOption particleData = new BlockParticleOption(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                        state);
                ParticleUtils.spawnParticleBelow(level, pos, random, particleData);
            }
        }
    }
}
