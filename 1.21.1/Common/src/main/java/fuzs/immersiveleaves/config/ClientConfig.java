package fuzs.immersiveleaves.config;

import fuzs.immersiveleaves.client.particle.FallingLeavesParticle;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ClientConfig implements ConfigCore {
    @Config
    public final FallingLeaves fallingLeaves = new FallingLeaves();
    @Config
    public final SnowyLeaves snowyLeaves = new SnowyLeaves();

    public static class FallingLeaves implements ConfigCore {
        @Config(description = {"Leaves blocks that will spawn leaf particles underneath.", ConfigDataSet.CONFIG_DESCRIPTION})
        List<String> leaveBlocksRaw = KeyedValueProvider.tagAppender(Registries.BLOCK).addTag(BlockTags.LEAVES).remove(
                Blocks.CHERRY_LEAVES).asStringList();
        @Config(description = "Maximum time in seconds leaf particles may exist in the world.")
        @Config.IntRange(min = 1)
        public int particleLifetime = 15;
        @Config(description = "Strength factor for the wind blowing the leaf particles in a certain direction.")
        @Config.DoubleRange(min = 0.0, max = 16.0)
        public double windStrength = 2.0;
        @Config(description = "Rain, snow and thunder will allow more leaf particles to spawn, and increase wind strength.")
        public boolean weatherEffects = true;
        @Config(description = "Wind direction in degrees for where leaves are blowing away to.")
        @Config.DoubleRange(min = 0.0, max = 360.0)
        public double windDirection = 0.0;
        @Config(description = "Decay speed for when leaf particles land on the ground.")
        public FallingLeavesParticle.DecayBehavior decayBehavior = FallingLeavesParticle.DecayBehavior.FAST;

        public ConfigDataSet<Block> leaveBlocks;

        @Override
        public void afterConfigReload() {
            this.leaveBlocks = ConfigDataSet.from(Registries.BLOCK, this.leaveBlocksRaw);
        }
    }

    public static class SnowyLeaves implements ConfigCore {

    }
}
