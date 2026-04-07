package com.leclowndu93150.cosycritters.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class BaseParticle extends SingleQuadParticle {

    protected BaseParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.hasPhysics = false;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
}
