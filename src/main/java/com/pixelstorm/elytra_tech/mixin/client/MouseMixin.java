package com.pixelstorm.elytra_tech.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.elytra_tech.CanFreeLook;
import com.pixelstorm.elytra_tech.ElytraTechClient;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Redirect(method = "updateLookDirection", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V"))
	private void changeFreeLookDirection(ClientPlayerEntity self, double cursorDeltaX, double cursorDeltaY) {
		CanFreeLook freeLooker = (CanFreeLook) self;
		if (ElytraTechClient.ELYTRA_FREE_LOOK_KEYBIND.isPressed()) {
			freeLooker.setFreeLooking(true);
			freeLooker.changeFreeLookDirection(cursorDeltaX, cursorDeltaY);
		} else {
			self.changeLookDirection(cursorDeltaX, cursorDeltaY);

			if (freeLooker.isFreeLooking()) {
				freeLooker.setFreeLookPitch(MathHelper.lerp(0.3f, freeLooker.getFreeLookPitch(), self.getPitch()));
				freeLooker.setFreeLookYaw(MathHelper.lerp(0.3f, freeLooker.getFreeLookYaw(), self.getYaw()));

				float pitchDiff = Math.abs(self.getPitch() - freeLooker.getFreeLookPitch());
				float yawDiff = Math.abs(self.getYaw() - freeLooker.getFreeLookYaw());

				if (pitchDiff <= 0.1 && yawDiff <= 0.1) {
					freeLooker.setFreeLooking(false);
				}
			} else {
				freeLooker.setFreeLookPitch(self.getPitch());
				freeLooker.setFreeLookYaw(self.getYaw());
			}
		}
	}
}
