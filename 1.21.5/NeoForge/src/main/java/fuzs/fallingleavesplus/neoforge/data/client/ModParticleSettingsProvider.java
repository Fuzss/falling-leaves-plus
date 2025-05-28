package fuzs.fallingleavesplus.neoforge.data.client;

import com.mojang.datafixers.util.Either;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class ModParticleSettingsProvider extends JsonCodecProvider<ParticleSettings> {

    public ModParticleSettingsProvider(DataProviderContext context) {
        this(context.getModId(), context.getPackOutput(), context.getRegistries());
    }

    public ModParticleSettingsProvider(String modId, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput,
                PackOutput.Target.RESOURCE_PACK,
                FallingLeavesManager.ASSET_DIRECTORY,
                ParticleSettings.CODEC,
                lookupProvider,
                modId);
    }

    @Override
    protected void gather() {
        this.block(Blocks.BIRCH_LEAVES);
        this.block(Blocks.SPRUCE_LEAVES);
        this.block(Blocks.JUNGLE_LEAVES);
        this.block(Blocks.ACACIA_LEAVES);
    }

    public final void block(Block block) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        List<ResourceLocation> textureLocations = IntStream.range(0, 12)
                .mapToObj(i -> FallingLeavesPlus.id(resourceLocation.getPath().replaceAll("leaves", "leaf_") + i))
                .toList();
        this.unconditional(resourceLocation,
                new ParticleSettings(Either.right(Optional.of(textureLocations)),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
    }
}
