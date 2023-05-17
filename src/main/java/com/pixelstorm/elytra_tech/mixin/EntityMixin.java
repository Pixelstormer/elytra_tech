package com.pixelstorm.elytra_tech.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.elytra_tech.ElytraTech;

@Mixin(Entity.class)
public class EntityMixin {
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
