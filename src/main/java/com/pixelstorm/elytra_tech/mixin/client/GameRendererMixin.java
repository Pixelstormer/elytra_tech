package com.pixelstorm.elytra_tech.mixin.client;

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

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Camera camera;

	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", ordinal = 0))
	private void modifyHandMatrix(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		Entity entity = camera.getFocusedEntity();
		if (entity instanceof CanFreeLook freeLooker && freeLooker.isFreeLooking()) {
			float pitchDiff = freeLooker.getFreeLookPitch() - entity.getPitch(tickDelta);
			float yawDiff = freeLooker.getFreeLookYaw() - entity.getYaw(tickDelta);
			matrices.multiply(Axis.X_POSITIVE.rotationDegrees(pitchDiff));
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(yawDiff));
		}
	}
}
