package com.leclowndu93150.cosycritters.util;

import net.minecraft.client.Camera;
import org.joml.Quaternionf;

public interface RotationOverride {
    enum FacingCameraMode {
        ROTATE_XYZ {
            @Override
            public void setRotation(Quaternionf quaternion, Camera camera, float tickPercent) {
                quaternion.set(camera.rotation());
            }
        },
        ROTATE_Y {
            @Override
            public void setRotation(Quaternionf quaternion, Camera camera, float tickPercent) {
                quaternion.rotationY(camera.getYRot() * ((float)Math.PI / 180F));
            }
        };

        public abstract void setRotation(Quaternionf quaternion, Camera camera, float tickPercent);
    }

    default void setParticleRotation(FacingCameraMode facingCameraMode, Quaternionf quaternion, Camera camera, float tickPercent) {
        facingCameraMode.setRotation(quaternion, camera, tickPercent);
    }
}
