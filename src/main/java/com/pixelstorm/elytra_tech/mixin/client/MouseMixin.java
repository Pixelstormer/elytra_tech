package com.pixelstorm.elytra_tech.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.elytra_tech.CanFreeLook;
import com.pixelstorm.elytra_tech.ElytraTechClient;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Redirect(method = "updateLookDirection", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V"))
	private void changeFreeLookDirection(ClientPlayerEntity self, double cursorDeltaX, double cursorDeltaY) {
		CanFreeLook freeLooker = (CanFreeLook) self;
		if (ElytraTechClient.ELYTRA_FREE_LOOK_KEYBIND.isPressed()) {
			freeLooker.changeFreeLookDirection(cursorDeltaX, cursorDeltaY);
		} else {
			self.changeLookDirection(cursorDeltaX, cursorDeltaY);
			freeLooker.setFreeLookPitch(self.getPitch());
			freeLooker.setFreeLookYaw(self.getYaw());
		}
	}
}
