package com.leclowndu93150.cosycritters.mixin;

import com.leclowndu93150.cosycritters.util.FacingCameraMode;
import com.leclowndu93150.cosycritters.util.RotationOverride;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin extends Particle implements RotationOverride {

    protected SingleQuadParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Redirect(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;rotation()Lorg/joml/Quaternionf;")
    )
    private Quaternionf setRotation(Camera camera) {
        Quaternionf quaternionf = new Quaternionf();
        setParticleRotation(FacingCameraMode.LOOKAT_XYZ, quaternionf, camera, 0.0f);
        return quaternionf;
    }
}