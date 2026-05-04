package fuzs.fallingleavesplus.common.client.particle.settings;

import fuzs.fallingleavesplus.common.FallingLeavesPlus;
import fuzs.fallingleavesplus.common.config.ClientConfig;
import fuzs.fallingleavesplus.common.core.particles.FallingLeavesParticleOption;
import fuzs.fallingleavesplus.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class FallingLeavesManager extends SimpleJsonResourceReloadListener<ParticleSettings> {
    public static final String ASSET_DIRECTORY = FallingLeavesPlus.MOD_ID + "/leaves";
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json(ASSET_DIRECTORY);

    @Nullable
    private static FallingLeavesManager instance;
    private Map<Block, ParticleSettings> particleSettings = Map.of();

    FallingLeavesManager() {
        super(ParticleSettings.CODEC, ASSET_LISTER);
    }

    public static void onAddResourcePackReloadListeners(BiConsumer<Identifier, PreparableReloadListener> consumer) {
        consumer.accept(FallingLeavesPlus.id("falling_leaves_manager"), instance = new FallingLeavesManager());
    }

    @Override
    protected void apply(Map<Identifier, ParticleSettings> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        IdentityHashMap<Block, ParticleSettings> map = new IdentityHashMap<>();
        for (Map.Entry<Identifier, ParticleSettings> entry : object.entrySet()) {
            if (BuiltInRegistries.BLOCK.containsKey(entry.getKey())) {
                map.put(BuiltInRegistries.BLOCK.getValue(entry.getKey()), entry.getValue());
            }
        }

        this.particleSettings = Collections.unmodifiableMap(map);
    }

    public static ParticleSettings getParticleSettings(BlockState blockState) {
        return getParticleSettings(blockState.getBlock());
    }

    public static ParticleSettings getParticleSettings(Block block) {
        FallingLeavesManager fallingLeavesManager = instance;
        if (fallingLeavesManager != null) {
            return instance.particleSettings.getOrDefault(block, ParticleSettings.DEFAULT);
        } else {
            return ParticleSettings.DEFAULT;
        }
    }

    public static void spawnFallingLeavesParticles(Level level, BlockState blockState, BlockPos blockPos, RandomSource randomSource) {
        ParticleSettings particleSettings = getParticleSettings(blockState);
        if (randomSource.nextFloat() < particleSettings.getLeafParticleChanceWithWeather(level)) {
            BlockState blockStateBelow = level.getBlockState(blockPos.below());
            if (!Block.isFaceFull(blockStateBelow.getCollisionShape(level, blockPos.below()), Direction.UP)) {
                ParticleOptions particleOptions = createLeavesParticles(level,
                        blockState,
                        blockPos,
                        particleSettings.getSpawnSnowflakes());
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, particleOptions);
            }
        }
    }

    private static ParticleOptions createLeavesParticles(Level level, BlockState blockState, BlockPos blockPos, boolean spawnSnowflakes) {
        if (spawnSnowflakes) {
            BlockState blockStateAbove = level.getBlockState(blockPos.above());
            if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(blockStateAbove.getBlock())) {
                ParticleType<FallingLeavesParticleOption> particleType = pickParticleType(true);
                return new FallingLeavesParticleOption(particleType, blockStateAbove);
            }
        }

        ParticleType<FallingLeavesParticleOption> particleType = pickParticleType(false);
        return new FallingLeavesParticleOption(particleType,
                blockState,
                level.getClientLeafTintColor(blockPos));
    }

    private static ParticleType<FallingLeavesParticleOption> pickParticleType(boolean isSnowflake) {
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).blockParticles) {
            return ModRegistry.TERRAIN_LEAVES_PARTICLE_TYPE.value();
        } else {
            return isSnowflake ? ModRegistry.FALLING_SNOWFLAKE_PARTICLE_TYPE.value() :
                    ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value();
        }
    }
}
