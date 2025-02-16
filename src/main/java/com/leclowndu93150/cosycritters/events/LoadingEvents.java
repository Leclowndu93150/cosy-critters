package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.leclowndu93150.cosycritters.particle.BirdParticle;
import com.leclowndu93150.cosycritters.particle.HatManParticle;
import com.leclowndu93150.cosycritters.particle.MothParticle;
import com.leclowndu93150.cosycritters.particle.SpiderParticle;
import com.leclowndu93150.cosycritters.registry.ParticleRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = CosyCritters.MODID, bus = EventBusSubscriber.Bus.MOD)
public class LoadingEvents {

    @SubscribeEvent
    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.BIRD.get(), BirdParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.HAT_MAN.get(), HatManParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.MOTH.get(), MothParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SPIDER.get(), SpiderParticle.Provider::new);
    }

}
