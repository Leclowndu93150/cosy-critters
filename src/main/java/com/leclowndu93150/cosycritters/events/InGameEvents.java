package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = CosyCritters.MODID, bus = EventBusSubscriber.Bus.GAME)
public class InGameEvents {

    @SubscribeEvent
    private static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        CosyCritters.birdCount = 0;
        CosyCritters.mothCount = 0;
    }

    @SubscribeEvent
    private static void onClientTick(ClientTickEvent.Pre event) {
        if (Minecraft.getInstance().player != null) {
            CosyCritters.tickHatManSpawnConditions(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        var dispatcher = event.getServer().getCommands().getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> cmd = net.minecraft.commands.Commands.literal(CosyCritters.MODID)
                .executes(ctx -> {
                    ctx.getSource().sendSystemMessage(Component.literal(String.format("Birds: %d/%d", CosyCritters.birdCount, CosyCritters.maxBirdCount)));
                    ctx.getSource().sendSystemMessage(Component.literal(String.format("Moths: %d/%d", CosyCritters.mothCount, CosyCritters.maxMothCount)));
                    ctx.getSource().sendSystemMessage(Component.literal(String.format("Daytime: %d", Minecraft.getInstance().level.dayTime())));
                    return 0;
                });
        dispatcher.register(cmd);
    }

}
