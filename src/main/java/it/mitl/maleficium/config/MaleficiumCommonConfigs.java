package it.mitl.maleficium.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MaleficiumCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> VAMPIRE_STRENGTH_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> VAMPIRE_HEALTH_INCREASE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEAR_IMMORTAL_VAMPIRES;

    static {
        BUILDER.push("Maleficium Common Configs");

        VAMPIRE_STRENGTH_MULTIPLIER = BUILDER.comment("The multiplier for a player vampire's strength. (0.1 = 10% increase)")
                .define("Vampire Strength Multiplier", 0.4);
        VAMPIRE_HEALTH_INCREASE = BUILDER.comment("The amount to increase a vampire's health by. (1 heart = 2.0)")
                .define("Vampire Health Increase", 10.0);
        NEAR_IMMORTAL_VAMPIRES = BUILDER.comment("If true, vampires will not die unless damaged by fire, sunlight, or a wooden sword.")
                .define("Near Immortal Vampires", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
