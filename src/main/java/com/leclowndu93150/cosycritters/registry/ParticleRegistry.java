package com.leclowndu93150.cosycritters.registry;

import com.leclowndu93150.cosycritters.CosyCritters;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, CosyCritters.MODID);

    public static final RegistryObject<SimpleParticleType> BIRD = PARTICLE_TYPES.register("bird",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HAT_MAN = PARTICLE_TYPES.register("hat_man",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MOTH = PARTICLE_TYPES.register("moth",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SPIDER = PARTICLE_TYPES.register("spider",
            () -> new SimpleParticleType(true));
}
