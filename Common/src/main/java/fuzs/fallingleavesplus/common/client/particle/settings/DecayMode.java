package fuzs.fallingleavesplus.common.client.particle.settings;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum DecayMode implements StringRepresentable {
    IMMEDIATE,
    FAST,
    SLOW;

    public static final StringRepresentable.EnumCodec<DecayMode> CODEC = StringRepresentable.fromEnum(DecayMode::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
