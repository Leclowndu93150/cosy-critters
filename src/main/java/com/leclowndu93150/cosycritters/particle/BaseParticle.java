package com.leclowndu93150.cosycritters.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public abstract class BaseParticle extends TextureSheetParticle {

    protected BaseParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

}
