package fuzs.fallingleavesplus.data.client;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.*;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModParticleSettingsProvider implements DataProvider {
    /**
     * @see net.minecraft.client.particle.FallingLeavesParticle.CherryProvider
     */
    private static final VanillaSettings CHERRY_LEAVES = VanillaSettings.of(0.25F, 2.0F, false, true, 0.0F);
    /**
     * @see net.minecraft.client.particle.FallingLeavesParticle.PaleOakProvider
     */
    private static final VanillaSettings PALE_OAK_LEAVES = VanillaSettings.of(0.07F, 10.0F, true, false, 0.021F);
    /**
     * @see Blocks#AZALEA_LEAVES
     * @see Blocks#FLOWERING_AZALEA_LEAVES
     */
    private static final int AZALEA_LEAVES_COLOR = 0XFF70922D;
    /**
     * {@link VanillaSettings#leafSize()} for textures with 3x3 dimensions.
     */
    private static final float SMALL_LEAF_SIZE = 0.6F;
    /**
     * {@link VanillaSettings#leafSize()} for textures with 5x5 dimensions.
     */
    private static final float MEDIUM_LEAF_SIZE = 1.0F;
    /**
     * {@link VanillaSettings#leafSize()} for textures with 7x7 dimensions.
     */
    private static final float LARGE_LEAF_SIZE = 1.4F;
    private static final List<ParticleTexture> OAK_TEXTURES = ParticleTexture.builder()
            .biomeTint(Identifier.withDefaultNamespace("leaf_0"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_1"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_2"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_3"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_4"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_5"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_6"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_7"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_8"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_9"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_10"))
            .biomeTint(Identifier.withDefaultNamespace("leaf_11"))
            .noTint(FallingLeavesPlus.id("oak_acorn"))
            .noTint(FallingLeavesPlus.id("oak_apple"))
            .noTint(FallingLeavesPlus.id("oak_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> BIRCH_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("birch_0"))
            .biomeTint(FallingLeavesPlus.id("birch_1"))
            .biomeTint(FallingLeavesPlus.id("birch_2"))
            .biomeTint(FallingLeavesPlus.id("birch_3"))
            .biomeTint(FallingLeavesPlus.id("birch_4"), SMALL_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("birch_5"), SMALL_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("birch_6"), SMALL_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("birch_7"), SMALL_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("birch_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> SPRUCE_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("spruce_0"))
            .biomeTint(FallingLeavesPlus.id("spruce_1"))
            .biomeTint(FallingLeavesPlus.id("spruce_2"))
            .biomeTint(FallingLeavesPlus.id("spruce_3"))
            .biomeTint(FallingLeavesPlus.id("spruce_4"))
            .biomeTint(FallingLeavesPlus.id("spruce_5"))
            .biomeTint(FallingLeavesPlus.id("spruce_6"))
            .biomeTint(FallingLeavesPlus.id("spruce_7"))
            .noTint(FallingLeavesPlus.id("spruce_branch"), LARGE_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("spruce_cone"))
            .build();
    private static final List<ParticleTexture> JUNGLE_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("jungle_0"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_1"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_2"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_3"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_4"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_5"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_6"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("jungle_7"), LARGE_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("jungle_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> ACACIA_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("acacia_0"))
            .biomeTint(FallingLeavesPlus.id("acacia_1"))
            .biomeTint(FallingLeavesPlus.id("acacia_2"))
            .biomeTint(FallingLeavesPlus.id("acacia_3"))
            .biomeTint(FallingLeavesPlus.id("acacia_4"))
            .biomeTint(FallingLeavesPlus.id("acacia_5"))
            .biomeTint(FallingLeavesPlus.id("acacia_6"))
            .biomeTint(FallingLeavesPlus.id("acacia_7"))
            .noTint(FallingLeavesPlus.id("acacia_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> DARK_OAK_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("dark_oak_0"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_1"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_2"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_3"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_4"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_5"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_6"))
            .biomeTint(FallingLeavesPlus.id("dark_oak_7"))
            .noTint(FallingLeavesPlus.id("dark_oak_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> AZALEA_TEXTURES = ParticleTexture.builder()
            .noTint(FallingLeavesPlus.id("azalea_0"))
            .noTint(FallingLeavesPlus.id("azalea_1"))
            .noTint(FallingLeavesPlus.id("azalea_2"))
            .noTint(FallingLeavesPlus.id("azalea_3"))
            .noTint(FallingLeavesPlus.id("azalea_4"))
            .noTint(FallingLeavesPlus.id("azalea_5"))
            .noTint(FallingLeavesPlus.id("azalea_6"))
            .noTint(FallingLeavesPlus.id("azalea_7"))
            .build();
    private static final List<ParticleTexture> FLOWERING_AZALEA_TEXTURES = ParticleTexture.builder()
            .noTint(FallingLeavesPlus.id("flowering_azalea_0"))
            .noTint(FallingLeavesPlus.id("flowering_azalea_1"))
            .noTint(FallingLeavesPlus.id("flowering_azalea_2"))
            .noTint(FallingLeavesPlus.id("flowering_azalea_3"))
            .noTint(FallingLeavesPlus.id("flowering_azalea_4"), SMALL_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("flowering_azalea_5"), SMALL_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("flowering_azalea_6"), SMALL_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("flowering_azalea_7"), SMALL_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> MANGROVE_TEXTURES = ParticleTexture.builder()
            .biomeTint(FallingLeavesPlus.id("mangrove_0"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_1"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_2"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_3"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_4"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_5"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_6"), LARGE_LEAF_SIZE)
            .biomeTint(FallingLeavesPlus.id("mangrove_7"), LARGE_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("mangrove_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> CHERRY_TEXTURES = ParticleTexture.builder()
            .noTint(Identifier.withDefaultNamespace("cherry_0"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_1"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_2"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_3"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_4"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_5"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_6"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_7"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_8"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_9"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_10"), SMALL_LEAF_SIZE)
            .noTint(Identifier.withDefaultNamespace("cherry_11"), SMALL_LEAF_SIZE)
            .noTint(FallingLeavesPlus.id("cherry_branch"), LARGE_LEAF_SIZE)
            .build();
    private static final List<ParticleTexture> PALE_OAK_TEXTURES = ParticleTexture.builder()
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_0"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_1"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_2"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_3"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_4"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_5"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_6"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_7"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_8"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_9"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_10"))
            .biomeTint(Identifier.withDefaultNamespace("pale_oak_11"))
            .noTint(FallingLeavesPlus.id("pale_oak_branch"), LARGE_LEAF_SIZE)
            .build();

    private final Map<Identifier, ParticleSettings> values = new LinkedHashMap<>();
    private final PackOutput.PathProvider pathProvider;

    public ModParticleSettingsProvider(DataProviderContext context) {
        this(context.getPackOutput());
    }

    public ModParticleSettingsProvider(PackOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK,
                FallingLeavesManager.ASSET_DIRECTORY);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.addParticleSettings();
        return DataProvider.saveAll(cachedOutput, ParticleSettings.CODEC, this.pathProvider, this.values);
    }

    public void addParticleSettings() {
        this.block(Blocks.OAK_LEAVES, ParticleSettings.fromTextures(OAK_TEXTURES));
        this.block(Blocks.BIRCH_LEAVES, ParticleSettings.fromTextures(BIRCH_TEXTURES));
        // increase lifetime for large trees
        this.block(Blocks.SPRUCE_LEAVES,
                ParticleSettings.builder()
                        .setTextures(SPRUCE_TEXTURES)
                        .setAdditionalSettings(AdditionalSettings.fixedLifetime(20))
                        .build());
        // increase lifetime for large trees
        this.block(Blocks.JUNGLE_LEAVES,
                ParticleSettings.builder()
                        .setTextures(JUNGLE_TEXTURES)
                        .setAdditionalSettings(AdditionalSettings.fixedLifetime(20))
                        .build());
        this.block(Blocks.ACACIA_LEAVES, ParticleSettings.fromTextures(ACACIA_TEXTURES));
        this.block(Blocks.DARK_OAK_LEAVES, ParticleSettings.fromTextures(DARK_OAK_TEXTURES));
        this.block(Blocks.AZALEA_LEAVES, ParticleSettings.fromTextures(AZALEA_TEXTURES));
        // spawn more pretty particles
        this.block(Blocks.FLOWERING_AZALEA_LEAVES,
                ParticleSettings.builder().setTextures(FLOWERING_AZALEA_TEXTURES).setLeafParticleChance(0.02F).build());
        this.block(Blocks.MANGROVE_LEAVES, ParticleSettings.fromTextures(MANGROVE_TEXTURES));
        // match vanilla spawn chance and settings
        this.block(Blocks.CHERRY_LEAVES,
                ParticleSettings.builder()
                        .setTextures(CHERRY_TEXTURES)
                        .setLeafParticleChance(0.1F)
                        .setSpawnSnowflakes(false)
                        .setVanillaSettings(CHERRY_LEAVES)
                        .build());
        // match vanilla spawn chance
        this.block(Blocks.PALE_OAK_LEAVES,
                ParticleSettings.builder().setTextures(PALE_OAK_TEXTURES).setLeafParticleChance(0.02F).build());
    }

    public final void block(Block block, ParticleSettings particleSettings) {
        Identifier identifier = BuiltInRegistries.BLOCK.getKey(block);
        if (this.values.putIfAbsent(identifier, particleSettings) != null) {
            throw new IllegalStateException("Duplicate particle settings: " + identifier);
        }
    }

    @Override
    public String getName() {
        return "Particle Settings";
    }
}
