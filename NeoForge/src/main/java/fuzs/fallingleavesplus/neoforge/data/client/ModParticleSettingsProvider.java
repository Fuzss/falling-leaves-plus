package fuzs.fallingleavesplus.neoforge.data.client;

import com.mojang.datafixers.util.Either;
import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModParticleSettingsProvider extends JsonCodecProvider<ParticleSettings> {
    static final int AZALEA_LEAVES_COLOR = -9399763;

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
        this.block(Blocks.BIRCH_LEAVES, "birch");
        this.block(Blocks.SPRUCE_LEAVES, "spruce");
        this.block(Blocks.JUNGLE_LEAVES, "jungle");
        this.block(Blocks.ACACIA_LEAVES, "acacia");
        this.block(Blocks.DARK_OAK_LEAVES, "dark_oak");
        this.block(Blocks.AZALEA_LEAVES, "azalea", Either.left(AZALEA_LEAVES_COLOR));
        this.block(Blocks.FLOWERING_AZALEA_LEAVES, "flowering_azalea", Either.left(AZALEA_LEAVES_COLOR));
        this.block(Blocks.MANGROVE_LEAVES, "mangrove");
        this.block(Blocks.CHERRY_LEAVES,
                new ParticleSettings(ParticleSettings.createTextureLocations(ResourceLocationHelper.withDefaultNamespace(
                        "cherry")), 0.01F, VanillaSettings.CHERRY_LEAVES));
        this.block(Blocks.PALE_OAK_LEAVES,
                new ParticleSettings(ParticleSettings.createTextureLocations(ResourceLocationHelper.withDefaultNamespace(
                        "pale_oak")), 0.02F, VanillaSettings.PALE_OAK_LEAVES));
    }

    public final void block(Block block, String texturePath) {
        this.block(block, texturePath, Either.right(Optional.empty()));
    }

    public final void block(Block block, String texturePath, Either<Integer, Optional<Boolean>> tint) {
        this.block(block,
                new ParticleSettings(Optional.of(ParticleSettings.createTextureLocations(FallingLeavesPlus.id(
                        texturePath))), tint, Optional.empty(), Optional.empty()));
    }

    public final void block(Block block, ParticleSettings particleSettings) {
        this.unconditional(BuiltInRegistries.BLOCK.getKey(block), particleSettings);
    }
}
