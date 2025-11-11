package fuzs.fallingleavesplus.data.client;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.FallingLeavesManager;
import fuzs.fallingleavesplus.client.particle.settings.ParticleSettings;
import fuzs.fallingleavesplus.client.particle.settings.ParticleTexture;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModParticleSettingsProvider implements DataProvider {
    public static final List<ParticleTexture> OAK_TEXTURES = Stream.of(ResourceLocationHelper.withDefaultNamespace(
                    "leaf_0"),
            ResourceLocationHelper.withDefaultNamespace("leaf_1"),
            ResourceLocationHelper.withDefaultNamespace("leaf_2"),
            ResourceLocationHelper.withDefaultNamespace("leaf_3"),
            ResourceLocationHelper.withDefaultNamespace("leaf_4"),
            ResourceLocationHelper.withDefaultNamespace("leaf_5"),
            ResourceLocationHelper.withDefaultNamespace("leaf_6"),
            ResourceLocationHelper.withDefaultNamespace("leaf_7"),
            ResourceLocationHelper.withDefaultNamespace("leaf_8"),
            ResourceLocationHelper.withDefaultNamespace("leaf_9"),
            ResourceLocationHelper.withDefaultNamespace("leaf_10"),
            ResourceLocationHelper.withDefaultNamespace("leaf_11")).map(ParticleTexture::biomeTint).toList();
    public static final List<ParticleTexture> BIRCH_TEXTURES = Stream.of(FallingLeavesPlus.id("birch_0"),
            FallingLeavesPlus.id("birch_1"),
            FallingLeavesPlus.id("birch_2"),
            FallingLeavesPlus.id("birch_3")).map(ParticleTexture::biomeTint).toList();
    public static final List<ParticleTexture> SPRUCE_TEXTURES = List.of(ParticleTexture.biomeTint(FallingLeavesPlus.id(
                    "spruce_0")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_1")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_2")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_3")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_4")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_5")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_6")),
            ParticleTexture.biomeTint(FallingLeavesPlus.id("spruce_7")),
            ParticleTexture.noTint(FallingLeavesPlus.id("spruce_8")),
            ParticleTexture.noTint(FallingLeavesPlus.id("spruce_10")));
    public static final List<ParticleTexture> JUNGLE_TEXTURES = Stream.of(FallingLeavesPlus.id("jungle_0"),
            FallingLeavesPlus.id("jungle_1"),
            FallingLeavesPlus.id("jungle_2"),
            FallingLeavesPlus.id("jungle_3")).map(ParticleTexture::biomeTint).toList();
    public static final List<ParticleTexture> ACACIA_TEXTURES = Stream.of(FallingLeavesPlus.id("acacia_0"),
            FallingLeavesPlus.id("acacia_1"),
            FallingLeavesPlus.id("acacia_2"),
            FallingLeavesPlus.id("acacia_3")).map(ParticleTexture::biomeTint).toList();
    public static final List<ParticleTexture> DARK_OAK_TEXTURES = Stream.of(FallingLeavesPlus.id("dark_oak_0"),
            FallingLeavesPlus.id("dark_oak_1"),
            FallingLeavesPlus.id("dark_oak_2"),
            FallingLeavesPlus.id("dark_oak_3")).map(ParticleTexture::biomeTint).toList();
    public static final List<ParticleTexture> AZALEA_TEXTURES = Stream.of(FallingLeavesPlus.id("azalea_0"),
            FallingLeavesPlus.id("azalea_1"),
            FallingLeavesPlus.id("azalea_2"),
            FallingLeavesPlus.id("azalea_3")).map(ParticleTexture::noTint).toList();
    public static final List<ParticleTexture> FLOWERING_AZALEA_TEXTURES = List.of(ParticleTexture.noTint(
                    FallingLeavesPlus.id("flowering_azalea_0")),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_1")),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_2")),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_3")),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_4"), 0.5F),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_5"), 0.5F),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_6"), 0.5F),
            ParticleTexture.noTint(FallingLeavesPlus.id("flowering_azalea_7"), 0.5F));
    public static final List<ParticleTexture> MANGROVE_TEXTURES = Stream.of(FallingLeavesPlus.id("mangrove_0"),
            FallingLeavesPlus.id("mangrove_1"),
            FallingLeavesPlus.id("mangrove_2"),
            FallingLeavesPlus.id("mangrove_3")).map(ParticleTexture::biomeTint).toList();
    private static final List<ParticleTexture> CHERRY_TEXTURES = Stream.of(ResourceLocationHelper.withDefaultNamespace(
                    "cherry_0"),
            ResourceLocationHelper.withDefaultNamespace("cherry_1"),
            ResourceLocationHelper.withDefaultNamespace("cherry_2"),
            ResourceLocationHelper.withDefaultNamespace("cherry_3"),
            ResourceLocationHelper.withDefaultNamespace("cherry_4"),
            ResourceLocationHelper.withDefaultNamespace("cherry_5"),
            ResourceLocationHelper.withDefaultNamespace("cherry_6"),
            ResourceLocationHelper.withDefaultNamespace("cherry_7"),
            ResourceLocationHelper.withDefaultNamespace("cherry_8"),
            ResourceLocationHelper.withDefaultNamespace("cherry_9"),
            ResourceLocationHelper.withDefaultNamespace("cherry_10"),
            ResourceLocationHelper.withDefaultNamespace("cherry_11")).map(ParticleTexture::noTint).toList();
    private static final List<ParticleTexture> PALE_OAK_TEXTURES = Stream.of(ResourceLocationHelper.withDefaultNamespace(
                    "pale_oak_0"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_1"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_2"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_3"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_4"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_5"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_6"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_7"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_8"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_9"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_10"),
            ResourceLocationHelper.withDefaultNamespace("pale_oak_11")).map(ParticleTexture::noTint).toList();
    private static final int AZALEA_LEAVES_COLOR = 0XFF70922D;
    /**
     * @see net.minecraft.client.particle.FallingLeavesParticle.CherryProvider
     */
    private static final VanillaSettings CHERRY_LEAVES = VanillaSettings.of(0.25F, 2.0F, false, true, 1.0F, 0.0F);
    /**
     * @see net.minecraft.client.particle.FallingLeavesParticle.PaleOakProvider
     */
    private static final VanillaSettings PALE_OAK_LEAVES = VanillaSettings.of(0.07F, 10.0F, true, false, 2.0F, 0.021F);

    private final Map<ResourceLocation, ParticleSettings> values = new LinkedHashMap<>();
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
        this.block(Blocks.OAK_LEAVES, ParticleSettings.builder().setTextures(OAK_TEXTURES).build());
        this.block(Blocks.BIRCH_LEAVES, ParticleSettings.builder().setTextures(BIRCH_TEXTURES).build());
        this.block(Blocks.SPRUCE_LEAVES, ParticleSettings.builder().setTextures(SPRUCE_TEXTURES).build());
        this.block(Blocks.JUNGLE_LEAVES,
                ParticleSettings.builder()
                        .setTextures(JUNGLE_TEXTURES)
                        .setVanillaSettings(VanillaSettings.fixedSize(2.4F))
                        .build());
        this.block(Blocks.ACACIA_LEAVES, ParticleSettings.builder().setTextures(ACACIA_TEXTURES).build());
        this.block(Blocks.DARK_OAK_LEAVES, ParticleSettings.builder().setTextures(DARK_OAK_TEXTURES).build());
        this.block(Blocks.AZALEA_LEAVES, ParticleSettings.builder().setTextures(AZALEA_TEXTURES).build());
        this.block(Blocks.FLOWERING_AZALEA_LEAVES,
                ParticleSettings.builder().setTextures(FLOWERING_AZALEA_TEXTURES).setLeafParticleChance(0.02F).build());
        this.block(Blocks.MANGROVE_LEAVES,
                ParticleSettings.builder()
                        .setTextures(MANGROVE_TEXTURES)
                        .setVanillaSettings(VanillaSettings.fixedSize(2.4F))
                        .build());
        this.block(Blocks.CHERRY_LEAVES,
                ParticleSettings.builder()
                        .setTextures(CHERRY_TEXTURES)
                        .setSpawnSnowflakes(false)
                        .setVanillaSettings(CHERRY_LEAVES)
                        .build());
        this.block(Blocks.PALE_OAK_LEAVES,
                ParticleSettings.builder().setTextures(PALE_OAK_TEXTURES).setLeafParticleChance(0.02F).build());
    }

    public final void block(Block block, ParticleSettings particleSettings) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        if (this.values.putIfAbsent(resourceLocation, particleSettings) != null) {
            throw new IllegalStateException("Duplicate particle settings: " + resourceLocation);
        }
    }

    @Override
    public String getName() {
        return "Particle Settings";
    }
}
