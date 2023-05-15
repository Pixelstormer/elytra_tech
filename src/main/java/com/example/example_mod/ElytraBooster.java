package com.example.example_mod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ElytraBooster {
	// Configurable parameters

	// Minimum time between each boost, measured in 1/60ths of a second(?)
	public int boostCooldown;
	// Amount that is added to velocity by each boost
	public float boostSpeed;

	// Internal state

	// Counts down from boostCooldown to 0 to measure the boost cooldown period
	private int boostCooldownTimer = 0;

	public enum BoostType {
		// A boost that doesn't actually add any velocity
		FAKE,
		// A boost in the direction the player is moving
		VELOCITY,
		// A boost in the direction the player is looking
		LOOKDIRECTION
	}

	public ElytraBooster() {
		this(20, 0.8f);
	}

	public ElytraBooster(int boostCooldown, float boostSpeed) {
		this.boostCooldown = boostCooldown;
		this.boostSpeed = boostSpeed;
	}

	public void tick() {
		if (this.boostCooldownTimer > 0) {
			this.boostCooldownTimer -= 1;
		}
	}

	public boolean isBoostReady() {
		return this.boostCooldownTimer <= 0;
	}

	public boolean tryInitiateBoost() {
		boolean ready = this.isBoostReady();
		if (ready) {
			this.boostCooldown = 20;
			// this.releasedLastBoost = false;
		}
		return ready;
	}

	public boolean fakeBoost() {
		return tryInitiateBoost();
	}

	public boolean velocityBoost(PlayerEntity self) {
		boolean shouldBoost = this.tryInitiateBoost();
		if (shouldBoost) {
			Vec3d velocity = self.getVelocity();
			double length = velocity.length() + this.boostSpeed;
			Vec3d normalised = velocity.normalize();
			Vec3d result = normalised.multiply(length);
			self.setVelocity(result);
		}
		return shouldBoost;
	}

	public boolean lookDirectionBoost(PlayerEntity self) {
		boolean shouldBoost = this.tryInitiateBoost();
		if (shouldBoost) {
			Vec3d rotation = self.getRotationVector();
			// Vec3d normalised = rotation.normalize();
			// Vec3d result = normalised.multiply(this.boostSpeed);
			// self.setVelocity(result);

			double d = 1.5;
			double e = 0.1;
			Vec3d velocity = self.getVelocity();
			self
					.setVelocity(
							velocity.add(
									rotation.x * e + (rotation.x * d - velocity.x) * 0.5,
									rotation.y * e + (rotation.y * d - velocity.y) * 0.5,
									rotation.z * e + (rotation.z * d - velocity.z) * 0.5));
		}
		return shouldBoost;
	}

}
