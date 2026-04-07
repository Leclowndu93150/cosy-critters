package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.leclowndu93150.cosycritters.particle.BirdParticle;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = CosyCritters.MODID)
public class InGameEvents {

    @SubscribeEvent
    private static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        BirdParticle.birds.clear();
        CosyCritters.mothCount = 0;
        CosyCritters.spiderCount = 0;
    }

    @SubscribeEvent
    private static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.level != null) {
            CosyCritters.tickHatManSpawnConditions(minecraft);
            CosyCritters.trySpawnBird(minecraft.level);
        }
    }
}
