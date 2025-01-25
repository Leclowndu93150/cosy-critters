package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.leclowndu93150.cosycritters.particle.BirdParticle;
import com.leclowndu93150.cosycritters.particle.HatManParticle;
import com.leclowndu93150.cosycritters.particle.MothParticle;
import com.leclowndu93150.cosycritters.particle.SpiderParticle;
import com.leclowndu93150.cosycritters.registry.ParticleRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = CosyCritters.MODID, bus = EventBusSubscriber.Bus.MOD)
public class LoadingEvents {

    @SubscribeEvent
    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.BIRD.get(), BirdParticle.DefaultFactory::new);
        event.registerSpriteSet(ParticleRegistry.HAT_MAN.get(), HatManParticle.DefaultFactory::new);
        event.registerSpriteSet(ParticleRegistry.MOTH.get(), MothParticle.DefaultFactory::new);
        event.registerSpriteSet(ParticleRegistry.SPIDER.get(), SpiderParticle.DefaultFactory::new);
    }

}
