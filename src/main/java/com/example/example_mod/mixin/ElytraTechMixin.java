package com.example.example_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ElytraTech;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class ElytraTechMixin extends LivingEntity implements ElytraTech {
	// Configurable parameters

	// Minimum time between each boost, measured in 1/60ths of a second(?)
	public int boostCooldown;
	// Amount that is added to velocity by each boost
	public float boostSpeed;

	// Internal state

	// Counts down from boostCooldown to 0 to measure the boost cooldown period
	private int boostCooldownTimer = 0;

	protected ElytraTechMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void elytraTech$onConstruct(CallbackInfo ci) {
		// TODO: load these values from a config file
		this.boostCooldown = 20;
		this.boostSpeed = 0.8f;
	}

	@Override
	public void tickElytraTech() {
		if (this.boostCooldownTimer > 0) {
			if (this.isFallFlying()) {
				this.boostCooldownTimer -= 1;
			} else {
				this.boostCooldownTimer = 0;
			}
		}
	}

	@Override
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
		Vec3d velocity = this.getVelocity();
		double length = velocity.length() + this.boostSpeed;
		Vec3d normalised = velocity.normalize();
		Vec3d result = normalised.multiply(length);
		this.setVelocity(result);
	}

	private void elytraTechLookDirectionBoost() {
		Vec3d rotation = this.getRotationVector();
		Vec3d boost = rotation.multiply(1.0 + this.boostSpeed);
		this.addVelocity(boost);
	}
}
