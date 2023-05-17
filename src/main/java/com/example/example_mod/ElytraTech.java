package com.example.example_mod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ElytraTech {
	// Configurable parameters

	// Minimum time between each boost, measured in 1/60ths of a second(?)
	public int boostCooldown;
	// Amount that is added to velocity by each boost
	public float boostSpeed;

	// Internal state

	// The player that this instance is associated with
	private PlayerEntity player;

	// Counts down from boostCooldown to 0 to measure the boost cooldown period
	private int boostCooldownTimer = 0;

	public ElytraTech(PlayerEntity player) {
		// TODO: load these values from a config file
		this(player, 20, 0.8f);
	}

	public ElytraTech(PlayerEntity player, int boostCooldown, float boostSpeed) {
		this.player = player;
		this.boostCooldown = boostCooldown;
		this.boostSpeed = boostSpeed;
	}

	public void tickElytraTech() {
		if (this.boostCooldownTimer > 0) {
			if (this.player.isFallFlying()) {
				this.boostCooldownTimer -= 1;
			} else {
				this.boostCooldownTimer = 0;
			}
		}
	}

	public boolean elytraTechBoost(ElytraBoostType type) {
		if (this.boostCooldownTimer > 0) {
			return false;
		}

		this.boostCooldownTimer = this.boostCooldown;
		switch (type) {
			case Velocity:
				this.elytraTechVelocityBoost();
				break;
			case LookDirection:
				this.elytraTechLookDirectionBoost();
				break;
			case Fake:
				break;
		}

		return true;
	}

	private void elytraTechVelocityBoost() {
		Vec3d velocity = this.player.getVelocity();
		double length = velocity.length() + this.boostSpeed;
		Vec3d normalised = velocity.normalize();
		Vec3d result = normalised.multiply(length);
		this.player.setVelocity(result);
	}

	private void elytraTechLookDirectionBoost() {
		Vec3d rotation = this.player.getRotationVector();
		Vec3d boost = rotation.multiply(1.0 + this.boostSpeed);
		this.player.addVelocity(boost);
	}

	public enum ElytraBoostType {
		// A boost in the direction the player is moving
		Velocity,
		// A boost in the direction the player is looking
		LookDirection,
		// A boost that doesn't actually add any speed
		Fake
	}
}
