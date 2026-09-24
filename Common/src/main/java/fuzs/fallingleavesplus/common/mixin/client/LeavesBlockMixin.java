package fuzs.fallingleavesplus.common.mixin.client;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.config.ClientConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingParticlesLeavesBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingParticlesLeavesBlock.class)
abstract class LeavesBlockMixin extends LeavesBlock {

    public LeavesBlockMixin(AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer, Properties properties) {
        super(ambientLeavesBlockSoundPlayer, properties);
    }

    @Inject(method = "makeFallingLeavesParticles", at = @At("HEAD"), cancellable = true)
    private void makeFallingLeavesParticles(Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo callback) {
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).defaultLeavesBlocks.contains(this)) {
            callback.cancel();
        }
    }
}
