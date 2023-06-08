package com.pixelstorm.elytra_tech.mixin.client;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pixelstorm.elytra_tech.CanFreeLook;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathConstants;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Camera camera;

	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"))
	private void modifyHandMatrix(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		Entity entity = camera.getFocusedEntity();
		if (entity instanceof CanFreeLook freeLooker && freeLooker.isFreeLooking()) {
			float pitchDiff = freeLooker.getFreeLookPitch() - entity.getPitch(tickDelta);
			float yawDiff = freeLooker.getFreeLookYaw() - entity.getYaw(tickDelta);

			Vector3f axis = new Vector3f(0.0f, 1.0f, 0.0f)
					.rotateX(freeLooker.getFreeLookPitch() * MathConstants.RADIANS_PER_DEGREE);

			matrices.multiply(new Quaternionf().rotationAxis(yawDiff * MathConstants.RADIANS_PER_DEGREE, axis));
			matrices.multiply(Axis.X_POSITIVE.rotationDegrees(pitchDiff));
		}
	}
}
