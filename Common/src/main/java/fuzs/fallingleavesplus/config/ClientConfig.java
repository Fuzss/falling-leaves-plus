package fuzs.fallingleavesplus.config;

import fuzs.fallingleavesplus.client.particle.settings.DecayMode;
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
    static final String CATEGORY_GENERAL = "general";

    @Config
    public final VanillaConfig vanilla = new VanillaConfig();
    @Config
    public final AdditionalConfig additional = new AdditionalConfig();
    @Config(
            name = "default_leave_blocks",
            category = CATEGORY_GENERAL,
            description = {"Leaves blocks that will spawn leaf particles underneath.", ConfigDataSet.CONFIG_DESCRIPTION}
    )
    List<String> defaultLeaveBlocksRaw = KeyedValueProvider.tagAppender(Registries.BLOCK)
            .addTag(BlockTags.LEAVES)
            .remove(Blocks.CHERRY_LEAVES)
            .remove(Blocks.PALE_OAK_LEAVES)
            .asStringList();
    @Config(category = CATEGORY_GENERAL, description = "Use block particles as leaves instead of leaf sprites.")
    public boolean blockParticles = false;
    @Config(
            category = CATEGORY_GENERAL,
            description = "The chance for a leaf particle to spawn below a leaves block on every animation tick."
    )
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double leafParticleChance = 0.01;
    @Config(
            category = CATEGORY_GENERAL,
            description = "Spawn snow flakes below snow covered leaves instead of falling leaves."
    )
    public boolean spawnSnowFlakes = true;

    public ConfigDataSet<Block> defaultLeaveBlocks;

    @Override
    public void afterConfigReload() {
        this.defaultLeaveBlocks = ConfigDataSet.from(Registries.BLOCK, this.defaultLeaveBlocksRaw);
    }

    public static class VanillaConfig implements ConfigCore {
        @Config(description = "Multiplier for the effect of gravity on leaf particles.")
        @Config.DoubleRange(min = 0.0)
        public double gravityMultiplier = 0.07;
        @Config(description = "Strength factor for the wind blowing the leaf particles in a certain direction.")
        @Config.DoubleRange(min = 0.0)
        public double windStrength = 10.0;
        @Config(description = "Determines if leaf particles swirl around as they fall.")
        public boolean swirlAround = true;
        @Config(description = "Determines if leaf particles flow away from obstacles.")
        public boolean flowAway = false;
        @Config(description = "The size of the leaf particles.")
        @Config.DoubleRange(min = 0.0)
        public double leafSize = 2.0;
        @Config(description = "The speed at which leaf particles fall.")
        @Config.DoubleRange(min = 0.0)
        public double initialFallingSpeed = 0.021;
    }

    public static class AdditionalConfig implements ConfigCore {
        @Config(description = "Rain and snow will allow more leaf particles to spawn, and increase wind strength.")
        @Config.DoubleRange(min = 0.0)
        public double rainAmplifier = 1.0;
        @Config(description = "Thunder will allow more leaf particles to spawn, and increase wind strength.")
        @Config.DoubleRange(min = 0.0)
        public double thunderstormAmplifier = 2.0;
        @Config(description = "Wind direction in degrees for where leaves are blowing away to.")
        @Config.DoubleRange(min = 0.0, max = 360.0)
        public double windDirection = 0.0;
        @Config(description = "Maximum time in seconds leaf particles may exist in the world.")
        @Config.IntRange(min = 0)
        public int lifetimeInSeconds = 15;
        @Config(description = "Determines if leaf particles collide with visual shapes of blocks rather than just their collision boxes.")
        public boolean collideWithVisualShapes = true;
        @Config(description = "Decay speed for when leaf particles land on the ground.")
        public DecayMode decayOnGroundMode = DecayMode.FAST;
    }
}
