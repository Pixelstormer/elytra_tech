package com.pixelstorm.elytra_tech.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.elytra_tech.CanFreeLook;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	private Entity focusedEntity;

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Redirect(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.setRotation(FF)V", ordinal = 0))
	private void setFreeLookRotation(Camera self, float yaw, float pitch) {
		if (this.focusedEntity instanceof CanFreeLook freeLooker && freeLooker.isFreeLooking()) {
			this.setRotation(freeLooker.getFreeLookYaw(), freeLooker.getFreeLookPitch());
		} else {
			this.setRotation(yaw, pitch);
		}
	}
}
