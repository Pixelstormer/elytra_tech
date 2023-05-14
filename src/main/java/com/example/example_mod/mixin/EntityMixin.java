package com.example.example_mod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.example.example_mod.ExampleMod;

@Mixin(Entity.class)
public class EntityMixin {
	@Redirect(method = "getRotationVector()Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;"))
	public Vec3d elytraTest$modifyFallFlying(Entity self, float pitch, float yaw) {
		if (self instanceof LivingEntity) {
			LivingEntity livingSelf = (LivingEntity) self;
			if (livingSelf.isFallFlying()) {
				if (((LivingEntityAccessor) livingSelf).getJumping()) {
					pitch = Math.copySign(pitch, -1.0f);
				}

				Vec3d movementInput = getMovementInput(livingSelf);
				// ExampleMod.LOGGER.info("movementInput is {}", movementInput);
				yaw = modifyYaw(yaw, movementInput);
			}
		}

		return ((EntityInvoker) self).invokeGetRotationVector(pitch, yaw);
	}

	private static Vec3d getMovementInput(LivingEntity self) {
		return new Vec3d((double) self.sidewaysSpeed, (double) self.upwardSpeed, (double) self.forwardSpeed);
	}

	private static float modifyYaw(float yaw, Vec3d movementInput) {
		int x = (int) Math.signum(movementInput.getX());
		int z = (int) Math.signum(movementInput.getZ());

		switch (x) {
			case -1:
				switch (z) {
					case -1:
						yaw += 135;
						break;
					case 0:
						yaw += 90;
						break;
					case 1:
						yaw += 45;
						break;
				}
				break;
			case 0:
				switch (z) {
					case -1:
						yaw += 180;
						break;
					case 0:
						break;
					case 1:
						break;
				}
				break;
			case 1:
				switch (z) {
					case -1:
						yaw += 225;
						break;
					case 0:
						yaw += 270;
						break;
					case 1:
						yaw += 315;
						break;
				}
				break;
		}

		return yaw;
	}
}
