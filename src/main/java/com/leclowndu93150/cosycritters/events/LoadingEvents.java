package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.leclowndu93150.cosycritters.particle.BirdParticle;
import com.leclowndu93150.cosycritters.particle.HatManParticle;
import com.leclowndu93150.cosycritters.particle.MothParticle;
import com.leclowndu93150.cosycritters.particle.SpiderParticle;
import com.leclowndu93150.cosycritters.registry.ParticleRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosyCritters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LoadingEvents {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.BIRD.get(), BirdParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.HAT_MAN.get(), HatManParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.MOTH.get(), MothParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SPIDER.get(), SpiderParticle.Provider::new);
    }

}
