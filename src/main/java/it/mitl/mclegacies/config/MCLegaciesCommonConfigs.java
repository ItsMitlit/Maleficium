package it.mitl.mclegacies.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MCLegaciesCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> VAMPIRE_STRENGTH_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> VAMPIRE_HEALTH_INCREASE;

    static {
        BUILDER.push("MCLegacies Common Configs");

        VAMPIRE_STRENGTH_MULTIPLIER = BUILDER.comment("The multiplier for a player vampire's strength. (0.1 = 10% increase)")
                .define("Vampire Strength Multiplier", 0.4);
        VAMPIRE_HEALTH_INCREASE = BUILDER.comment("The amount to increase a vampire's health by. (1 heart = 2.0)")
                .define("Vampire Health Increase", 10.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
