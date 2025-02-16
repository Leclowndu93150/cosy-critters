package com.leclowndu93150.cosycritters.util;

import net.minecraft.client.Camera;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public interface FacingCameraMode {
    FacingCameraMode LOOKAT_XYZ = (quaternion, camera, partialTicks) -> quaternion.set(camera.rotation());
    FacingCameraMode LOOKAT_Y = (quaternion, camera, partialTicks) -> quaternion.set(0.0F, camera.rotation().y, 0.0F, camera.rotation().w);

    void setRotation(Quaternionf quaternion, Camera camera, float partialTicks);
}
