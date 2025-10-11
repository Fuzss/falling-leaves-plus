package fuzs.fallingleavesplus.client.particle;

import fuzs.fallingleavesplus.FallingLeavesPlus;
import fuzs.fallingleavesplus.client.particle.settings.AdditionalSettings;
import fuzs.fallingleavesplus.client.particle.settings.VanillaSettings;
import fuzs.fallingleavesplus.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainFallingLeavesParticle extends CustomFallingLeavesParticle {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public TerrainFallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, BlockState blockState, VanillaSettings vanillaSettings, AdditionalSettings additionalSettings) {
        super(level, x, y, z, sprite, vanillaSettings, additionalSettings);
        this.pos = BlockPos.containing(x, y, z);
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (FallingLeavesPlus.CONFIG.get(ClientConfig.class).snowflakesSpawningBlocks.contains(blockState.getBlock())) {
            this.quadSize /= 2.0F;
            this.setSize(this.quadSize, this.quadSize);
        }
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public Layer getLayer() {
        return Layer.TERRAIN;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }
}
