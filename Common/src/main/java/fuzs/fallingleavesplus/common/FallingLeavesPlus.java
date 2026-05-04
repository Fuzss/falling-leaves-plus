package fuzs.fallingleavesplus.common;

import fuzs.fallingleavesplus.common.config.ClientConfig;
import fuzs.fallingleavesplus.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallingLeavesPlus implements ModConstructor {
    public static final String MOD_ID = "fallingleavesplus";
    public static final String MOD_NAME = "Falling Leaves Plus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
