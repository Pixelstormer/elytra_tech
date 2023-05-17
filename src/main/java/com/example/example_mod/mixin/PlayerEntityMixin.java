package com.example.example_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ElytraTech;
import com.example.example_mod.HasElytraTech;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HasElytraTech {
	private ElytraTech elytraTech;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void elytraTech$onConstruct(CallbackInfo ci) {
		this.elytraTech = new ElytraTech((PlayerEntity) (Object) this);
	}

	@Override
	public ElytraTech getElytraTech() {
		return this.elytraTech;
	}

	@Override
	public void setElytraTech(ElytraTech elytraTech) {
		this.elytraTech = elytraTech;
	}

	@Inject(method = "startFallFlying", at = @At("RETURN"))
	private void elytraTech$onStartFallFlying(CallbackInfo ci) {
		ElytraTech tech = ((HasElytraTech) this).getElytraTech();
		tech.onTakeoff();
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void elytraTech$onTickMovement(CallbackInfo ci) {
		ElytraTech tech = ((HasElytraTech) this).getElytraTech();
		tech.tick();
	}
}
