package melonslise.locks.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.regex.Pattern;

public final class LocksConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue RANDOMIZE_LOADED_LOCKS;

    static {
        ForgeConfigSpec.Builder cfg = new ForgeConfigSpec.Builder();

        RANDOMIZE_LOADED_LOCKS = cfg
                .comment("Randomize lock IDs and combinations when loading them from a structure file. Randomization works just like during world generation")
                .define("Randomize Loaded Locks", true);

        SPEC = cfg.build();
    }

    private LocksConfig() {}
}