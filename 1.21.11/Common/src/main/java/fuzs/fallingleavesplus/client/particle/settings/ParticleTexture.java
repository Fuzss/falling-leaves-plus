package fuzs.fallingleavesplus.client.particle.settings;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record ParticleTexture(Identifier id, Optional<Float> textureScale, Either<Integer, Boolean> tint) {
    public static final MapCodec<Either<Integer, Boolean>> TINT_CODEC = Codec.mapEither(ExtraCodecs.ARGB_COLOR_CODEC.fieldOf(
            "tint_color"), Codec.BOOL.lenientOptionalFieldOf("tinted", Boolean.FALSE));
    private static final Codec<ParticleTexture> FULL_CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<ParticleTexture> instance) -> {
        return instance.group(Identifier.CODEC.fieldOf("asset_id").forGetter(ParticleTexture::id),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("texture_scale").forGetter(ParticleTexture::textureScale),
                TINT_CODEC.forGetter(ParticleTexture::tint)).apply(instance, ParticleTexture::new);
    });
    public static final Codec<ParticleTexture> CODEC = Codec.either(Identifier.CODEC, FULL_CODEC)
            .xmap((Either<Identifier, ParticleTexture> either) -> {
                return either.map(ParticleTexture::noTint, Function.identity());
            }, (ParticleTexture particleTexture) -> {
                return particleTexture.isComplex() ? Either.right(particleTexture) : Either.left(particleTexture.id);
            });
    public static final List<ParticleTexture> DEFAULT = ParticleTexture.builder()
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
            .build();

    public static ParticleTexture noTint(Identifier id) {
        return new ParticleTexture(id, Optional.empty(), Either.right(Boolean.FALSE));
    }

    public static ParticleTexture noTint(Identifier id, float textureScale) {
        return new ParticleTexture(id, Optional.of(textureScale), Either.right(Boolean.FALSE));
    }

    public static ParticleTexture biomeTint(Identifier id) {
        return new ParticleTexture(id, Optional.empty(), Either.right(Boolean.TRUE));
    }

    public static ParticleTexture biomeTint(Identifier id, float textureScale) {
        return new ParticleTexture(id, Optional.of(textureScale), Either.right(Boolean.TRUE));
    }

    public static ParticleTexture fixedTint(Identifier id, int tintColor) {
        return new ParticleTexture(id, Optional.empty(), Either.left(tintColor));
    }

    private boolean isComplex() {
        return this.isTinted() || this.textureScale.isPresent();
    }

    public boolean isTinted() {
        return this.tint().right().orElse(Boolean.TRUE);
    }

    public int pickTintColor(int tintColor) {
        return this.tint().left().orElse(tintColor);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ParticleTexture> textures = new ArrayList<>();

        public Builder noTint(Identifier id) {
            this.textures.add(ParticleTexture.noTint(id));
            return this;
        }

        public Builder noTint(Identifier id, float textureScale) {
            this.textures.add(ParticleTexture.noTint(id, textureScale));
            return this;
        }

        public Builder biomeTint(Identifier id) {
            this.textures.add(ParticleTexture.biomeTint(id));
            return this;
        }

        public Builder biomeTint(Identifier id, float textureScale) {
            this.textures.add(ParticleTexture.biomeTint(id, textureScale));
            return this;
        }

        public Builder fixedTint(Identifier id, int tintColor) {
            this.textures.add(ParticleTexture.fixedTint(id, tintColor));
            return this;
        }

        public List<ParticleTexture> build() {
            return ImmutableList.copyOf(this.textures);
        }
    }
}
