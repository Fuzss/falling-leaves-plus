package fuzs.fallingleavesplus.client.particle.settings;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ParticleSettings(Optional<ResourceLocation> spriteLocation,
                               Optional<BehavioralSettings> behavior,
                               Optional<EnvironmentalSettings> environment) {

    public BehavioralSettings getBehavior() {
        return this.behavior.orElse(BehavioralSettings.DEFAULT);
    }

    public EnvironmentalSettings getEnvironment() {
        return this.environment.orElse(EnvironmentalSettings.DEFAULT);
    }
}
