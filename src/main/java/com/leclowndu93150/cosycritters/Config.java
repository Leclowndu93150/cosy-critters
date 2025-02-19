package com.leclowndu93150.cosycritters;


import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static ModConfigSpec.IntValue maxBirds;
    public static ModConfigSpec.IntValue maxMoths;
    public static ModConfigSpec.BooleanValue enableBirds;
    public static ModConfigSpec.BooleanValue enableMoths;
    public static ModConfigSpec.BooleanValue enableSpiders;
    public static ModConfigSpec.BooleanValue enableHatMan;

    public static void register(ModContainer modContainer) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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

        modContainer.registerConfig(ModConfig.Type.CLIENT, builder.build());
    }
}
