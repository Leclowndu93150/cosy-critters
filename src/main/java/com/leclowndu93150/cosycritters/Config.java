package com.leclowndu93150.cosycritters;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static ModConfigSpec.IntValue maxBirds;
    public static ModConfigSpec.IntValue maxMoths;
    public static ModConfigSpec.IntValue maxSpiders;
    public static ModConfigSpec.BooleanValue enableBirds;
    public static ModConfigSpec.BooleanValue enableMoths;
    public static ModConfigSpec.BooleanValue enableSpiders;
    public static ModConfigSpec.BooleanValue enableHatMan;

    public static ModConfigSpec.IntValue birdReactionDistance;
    public static ModConfigSpec.IntValue birdReactionSpeed;
    public static ModConfigSpec.IntValue birdDespawnDistance;
    public static ModConfigSpec.IntValue birdBlockAvoidanceDistance;
    public static ModConfigSpec.DoubleValue birdBlockAvoidanceFactor;
    public static ModConfigSpec.IntValue birdSeparationDistance;
    public static ModConfigSpec.DoubleValue birdSeparation;
    public static ModConfigSpec.IntValue birdFlockRange;
    public static ModConfigSpec.DoubleValue birdAlignment;
    public static ModConfigSpec.DoubleValue birdCohesion;
    public static ModConfigSpec.DoubleValue birdMaxSpeed;
    public static ModConfigSpec.DoubleValue birdMinSpeed;
    public static ModConfigSpec.IntValue birdMaxBehaviourTime;
    public static ModConfigSpec.DoubleValue birdLandingResponsiveness;
    public static ModConfigSpec.IntValue birdFlightHeightLimit;
    public static ModConfigSpec.DoubleValue birdFlightHeightLimitFactor;
    public static ModConfigSpec.DoubleValue birdFlightRandomness;

    public static void register(ModContainer modContainer) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Spawning");
        enableBirds = builder.define("enableBirds", true);
        maxBirds = builder.defineInRange("maxBirds", 50, 0, 200);
        enableMoths = builder.define("enableMoths", true);
        maxMoths = builder.defineInRange("maxMoths", 8, 0, 100);
        enableSpiders = builder.define("enableSpiders", true);
        maxSpiders = builder.defineInRange("maxSpiders", 16, 0, 100);
        enableHatMan = builder.define("enableHatMan", true);
        builder.pop();

        builder.push("Birds");
        birdReactionDistance = builder.defineInRange("reactionDistance", 10, 1, 64);
        birdReactionSpeed = builder.defineInRange("reactionSpeed", 20, 1, 200);
        birdDespawnDistance = builder.defineInRange("despawnDistance", 128, 32, 512);
        birdBlockAvoidanceDistance = builder.defineInRange("blockAvoidanceDistance", 8, 1, 32);
        birdBlockAvoidanceFactor = builder.defineInRange("blockAvoidanceFactor", 0.1, 0.0, 1.0);
        birdSeparationDistance = builder.defineInRange("separationDistance", 2, 1, 16);
        birdSeparation = builder.defineInRange("separation", 0.01, 0.0, 1.0);
        birdFlockRange = builder.defineInRange("flockRange", 20, 1, 64);
        birdAlignment = builder.defineInRange("alignment", 0.05, 0.0, 1.0);
        birdCohesion = builder.defineInRange("cohesion", 0.001, 0.0, 1.0);
        birdMaxSpeed = builder.defineInRange("maxSpeed", 1.0, 0.1, 5.0);
        birdMinSpeed = builder.defineInRange("minSpeed", 0.3, 0.01, 5.0);
        birdMaxBehaviourTime = builder.defineInRange("maxBehaviourTime", 400, 50, 2000);
        birdLandingResponsiveness = builder.defineInRange("landingResponsiveness", 0.01, 0.001, 1.0);
        birdFlightHeightLimit = builder.defineInRange("flightHeightLimit", 20, 5, 100);
        birdFlightHeightLimitFactor = builder.defineInRange("flightHeightLimitFactor", 0.005, 0.001, 0.1);
        birdFlightRandomness = builder.defineInRange("flightRandomness", 0.03, 0.0, 1.0);
        builder.pop();

        modContainer.registerConfig(ModConfig.Type.CLIENT, builder.build());
    }
}
