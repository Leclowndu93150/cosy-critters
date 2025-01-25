package com.leclowndu93150.cosycritters.registry;

import com.leclowndu93150.cosycritters.CosyCritters;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, CosyCritters.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIRD = PARTICLE_TYPES.register("bird",
            () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HAT_MAN = PARTICLE_TYPES.register("hat_man",
            () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOTH = PARTICLE_TYPES.register("moth",
            () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPIDER = PARTICLE_TYPES.register("spider",
            () -> new SimpleParticleType(true));
}
