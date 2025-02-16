package com.leclowndu93150.cosycritters.events;

import com.leclowndu93150.cosycritters.CosyCritters;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosyCritters.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InGameEvents {

    @SubscribeEvent
    public static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        CosyCritters.birdCount = 0;
        CosyCritters.mothCount = 0;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;
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
