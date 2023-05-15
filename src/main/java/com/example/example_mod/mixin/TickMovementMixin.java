package com.example.example_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ElytraTech;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class TickMovementMixin extends LivingEntity {
	protected TickMovementMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickMovement()V", at = @At("HEAD"))
	private void elytraTech$onTickMovement(CallbackInfo ci) {
		ElytraTech tech = (ElytraTech) this;
		tech.tickElytraTech();
	}
}
