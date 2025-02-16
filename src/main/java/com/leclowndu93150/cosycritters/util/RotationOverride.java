package com.leclowndu93150.cosycritters.util;

import net.minecraft.client.Camera;
import org.joml.Quaternionf;

public interface RotationOverride {
    default void setParticleRotation(FacingCameraMode facingCameraMode, Quaternionf quaternionf, Camera camera, float tickPercent) {
        facingCameraMode.setRotation(quaternionf, camera, tickPercent);
    }
}
