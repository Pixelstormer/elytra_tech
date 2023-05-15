package com.example.example_mod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ExampleMod;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Redirect(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;limitFallDistance()V"))
	public void elytraTest$jumpBoost(LivingEntity self) {
		// boolean isJumping = ((LivingEntityAccessor) self).getJumping();

		// if (!isJumping) {
		// this.releasedLastBoost = true;
		// }

		// if (self.isFallFlying()) {
		// if (this.boostCooldown > 0) {
		// this.boostCooldown -= 1;
		// } else if (isJumping && this.releasedLastBoost) {
		// this.boostCooldown = 20;
		// this.releasedLastBoost = false;
		// boost(self);
		// }
		// } else {
		// this.boostCooldown = 0;
		// }

		self.limitFallDistance();
	}

	// This may be too cheap?
	@Redirect(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
	public float elytraTest$modifyPitch(LivingEntity self) {
		float pitch = self.getPitch();
		if (self.isFallFlying() && self.isSneaking()) {
			pitch = Math.max(Math.copySign(pitch, -1.0f), -45.0f);
		}
		return pitch;
	}
}
