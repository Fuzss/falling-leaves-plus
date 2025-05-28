package fuzs.fallingleavesplus.client.particle.settings;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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

    public static void onAddResourcePackReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> consumer) {
        consumer.accept(FallingLeavesPlus.id("falling_leaves_manager"), instance = new FallingLeavesManager());
    }

    @Override
    protected void apply(Map<ResourceLocation, ParticleSettings> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        IdentityHashMap<Block, ParticleSettings> map = new IdentityHashMap<>();
        for (Map.Entry<ResourceLocation, ParticleSettings> entry : object.entrySet()) {
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

    public static void makeFallingLeavesParticles(Level level, BlockState blockState, BlockPos blockPos, RandomSource randomSource) {
        ParticleSettings particleSettings = getParticleSettings(blockState);
        if (!(randomSource.nextFloat() >= particleSettings.getLeafParticleChanceWithWeather(level))) {
            if (!Block.isFaceFull(level.getBlockState(blockPos.below()).getCollisionShape(level, blockPos.below()),
                    Direction.UP)) {
                BlockParticleOption blockParticleOption = new BlockParticleOption(ModRegistry.FALLING_LEAVES_PARTICLE_TYPE.value(),
                        blockState);
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, blockParticleOption);
            }
        }
    }
}
