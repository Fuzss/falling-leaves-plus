package fuzs.fallingleavesplus.common.mixin.client;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.config.ClientConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CherryLeavesBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CherryLeavesBlock.class)
abstract class CherryLeavesBlockMixin extends LeavesBlock {

    public CherryLeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "animateTick",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/block/LeavesBlock;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
                     shift = At.Shift.AFTER),
            cancellable = true)
    private void makeFallingLeavesParticles(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo callback) {
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).defaultLeavesBlocks.contains(this)) {
            callback.cancel();
        }
    }
}
