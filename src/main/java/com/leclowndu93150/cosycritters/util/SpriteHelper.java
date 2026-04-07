package com.leclowndu93150.cosycritters.util;

import com.leclowndu93150.cosycritters.CosyCritters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public class SpriteHelper {

    public static TextureAtlasSprite getSprite(String path) {
        return Minecraft.getInstance().getAtlasManager()
                .getAtlasOrThrow(AtlasIds.PARTICLES)
                .getSprite(Identifier.fromNamespaceAndPath(CosyCritters.MODID, path));
    }
}
