package fuzs.fallingleavesplus.client.particle.settings;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public record ParticleTexture(ResourceLocation id, float textureScale, Either<Integer, Boolean> tint) {
    public static final MapCodec<Either<Integer, Boolean>> TINT_CODEC = Codec.mapEither(ExtraCodecs.ARGB_COLOR_CODEC.fieldOf(
            "tint_color"), Codec.BOOL.lenientOptionalFieldOf("tinted", Boolean.FALSE));
    private static final Codec<ParticleTexture> FULL_CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<ParticleTexture> instance) -> {
        return instance.group(ResourceLocation.CODEC.fieldOf("asset_id").forGetter(ParticleTexture::id),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("texture_scale", 1.0F)
                        .forGetter(ParticleTexture::textureScale),
                TINT_CODEC.forGetter(ParticleTexture::tint)).apply(instance, ParticleTexture::new);
    });
    public static final Codec<ParticleTexture> CODEC = Codec.either(ResourceLocation.CODEC, FULL_CODEC)
            .xmap((Either<ResourceLocation, ParticleTexture> either) -> {
                return either.map(ParticleTexture::noTint, Function.identity());
            }, (ParticleTexture particleTexture) -> {
                return particleTexture.isComplex() ? Either.right(particleTexture) : Either.left(particleTexture.id);
            });

    public static ParticleTexture noTint(ResourceLocation id) {
        return noTint(id, 1.0F);
    }

    public static ParticleTexture noTint(ResourceLocation id, float textureScale) {
        return new ParticleTexture(id, textureScale, Either.right(Boolean.FALSE));
    }

    public static ParticleTexture biomeTint(ResourceLocation id) {
        return biomeTint(id, 1.0F);
    }

    public static ParticleTexture biomeTint(ResourceLocation id, float textureScale) {
        return new ParticleTexture(id, textureScale, Either.right(Boolean.TRUE));
    }

    public static ParticleTexture fixedTint(ResourceLocation id, int tintColor) {
        return new ParticleTexture(id, 1.0F, Either.left(tintColor));
    }

    private boolean isComplex() {
        return this.isTinted() || this.textureScale != 1.0F;
    }

    public boolean isTinted() {
        return this.tint().right().orElse(Boolean.TRUE);
    }

    public int pickTintColor(int tintColor) {
        return this.tint().left().orElse(tintColor);
    }
}
