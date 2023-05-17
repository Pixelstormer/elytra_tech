package com.pixelstorm.elytra_tech.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pixelstorm.elytra_tech.ElytraBooster;
import com.pixelstorm.elytra_tech.HasElytraBooster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HasElytraBooster {
	private ElytraBooster elytraBooster;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void elytraTech$onConstruct(CallbackInfo ci) {
		this.elytraBooster = new ElytraBooster((PlayerEntity) (Object) this);
	}

	@Override
	public ElytraBooster getElytraBooster() {
		return this.elytraBooster;
	}

	@Override
	public void setElytraBooster(ElytraBooster elytraBooster) {
		this.elytraBooster = elytraBooster;
	}

	@Inject(method = "startFallFlying", at = @At("RETURN"))
	private void elytraTech$onStartFallFlying(CallbackInfo ci) {
		ElytraBooster booster = ((HasElytraBooster) this).getElytraBooster();
		booster.onTakeoff();
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void elytraTech$onTickMovement(CallbackInfo ci) {
		ElytraBooster booster = ((HasElytraBooster) this).getElytraBooster();
		booster.tick();
	}
}
