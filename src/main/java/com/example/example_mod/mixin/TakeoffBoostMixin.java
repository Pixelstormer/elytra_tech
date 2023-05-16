package com.example.example_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ElytraTech;
import com.example.example_mod.ElytraTech.ElytraBoostType;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class TakeoffBoostMixin extends LivingEntity {
	protected TakeoffBoostMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	public boolean shouldTakeoffBoost(float pitch) {
		// Only boost if the player is looking upwards
		return pitch < 0.0f;
	}

	@Inject(method = "startFallFlying()V", at = @At("RETURN"))
	private void elytraTech$performTakeoffBoost(CallbackInfo ci) {
		ElytraTech tech = (ElytraTech) this;
		if (this.shouldTakeoffBoost(this.getPitch())) {
			tech.elytraTechBoost(ElytraBoostType.LookDirection);
		} else {
			tech.elytraTechBoost(ElytraBoostType.Fake);
		}
	}
}
