package com.leclowndu93150.cosycritters;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
    public static ForgeConfigSpec.IntValue maxBirds;
    public static ForgeConfigSpec.IntValue maxMoths;
    public static ForgeConfigSpec.BooleanValue enableBirds;
    public static ForgeConfigSpec.BooleanValue enableMoths;
    public static ForgeConfigSpec.BooleanValue enableSpiders;
    public static ForgeConfigSpec.BooleanValue enableHatMan;

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Cosy Critters Configuration");

        builder.push("Spawning");

        enableBirds = builder.comment("Enable bird particles")
                .define("enableBirds", true);
        maxBirds = builder.comment("Maximum number of birds that can exist at once")
                .defineInRange("maxBirds", 10, 0, 100);

        enableMoths = builder.comment("Enable moth particles")
                .define("enableMoths", true);
        maxMoths = builder.comment("Maximum number of moths that can exist at once")
                .defineInRange("maxMoths", 10, 0, 100);

        enableSpiders = builder.comment("Enable spider particles")
                .define("enableSpiders", true);

        enableHatMan = builder.comment("Enable the Hat Man (appears during full moon while sleeping)")
                .define("enableHatMan", true);

        builder.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
}

