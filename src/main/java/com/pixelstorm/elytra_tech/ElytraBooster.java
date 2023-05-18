package com.pixelstorm.elytra_tech;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ElytraBooster {
	// Configurable parameters

	// Amount that is added to velocity by boosting
	public float boostSpeed;

	// Minimum time between each boost, measured in ticks (1/20ths of a second?)
	public int boostCooldown;

	// Internal state

	// The player that this instance is associated with
	private PlayerEntity player;

	// Gets set to {@link #boostCooldown} when a boost happens, and counts down to 0
	// each tick. Is always 0 when not flying.
	private int boostCooldownTimer;

	public ElytraBooster(PlayerEntity player) {
		// TODO: load these values from a config file
		this(player, 0.8f, 20);
	}

	public ElytraBooster(PlayerEntity player, float boostSpeed, int boostCooldown) {
		this.player = player;
		this.boostSpeed = boostSpeed;
		this.boostCooldown = boostCooldown;
		this.boostCooldownTimer = 0;
	}

	public void tick() {
		if (this.boostCooldownTimer > 0) {
			if (this.player.isFallFlying()) {
				this.boostCooldownTimer -= 1;
			} else {
				this.boostCooldownTimer = 0;
			}
		}
	}

	public void onTakeoff() {
		// Only boost if the player is looking upwards
		if (this.player.getPitch() < 0.0f) {
			this.boost();
		}
	}

	public boolean boost() {
		// Only boost if it's off cooldown
		boolean shouldBoost = this.boostCooldownTimer <= 0;
		if (shouldBoost) {
			this.lookDirectionBoost(this.boostSpeed);
			this.boostCooldownTimer = this.boostCooldown;
			this.player.getWorld().playSoundFromEntity(player, player,
					SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.35f, 1.1f);
		}
		return shouldBoost;
	}

	private void lookDirectionBoost(float boostSpeed) {
		Vec3d rotation = this.player.getRotationVector();
		Vec3d boost = rotation.multiply(boostSpeed);
		this.player.addVelocity(boost);
	}

	private void velocityBoost(float boostSpeed) {
		Vec3d velocity = this.player.getVelocity();
		double length = velocity.length() + boostSpeed;
		Vec3d normalised = velocity.normalize();
		Vec3d result = normalised.multiply(length);
		this.player.setVelocity(result);
	}
}
