package com.example.example_mod.mixin.client;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.example_mod.ElytraTech;
import com.example.example_mod.ExampleMod;
import com.example.example_mod.ElytraTech.ElytraBoostType;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	@Shadow
	public Input input;

	private boolean wasJumpingPreviousTick;
	private boolean wasFallFlyingPreviousTick;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
		throw new AssertionError();
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void getPreviousTickState(CallbackInfo ci) {
		this.wasJumpingPreviousTick = this.input.jumping;
		this.wasFallFlyingPreviousTick = this.isFallFlying();
	}

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isFallFlying()Z"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"), to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;shouldSwimInFluids()Z")))
	private void doMidAirBoost(CallbackInfo ci) {
		boolean startedJumpingThisTick = this.input.jumping && !this.wasJumpingPreviousTick;
		boolean startedFallFlyingThisTick = this.isFallFlying() && !this.wasFallFlyingPreviousTick;
		if (startedJumpingThisTick && this.isFallFlying() && !startedFallFlyingThisTick) {
			ElytraTech tech = (ElytraTech) this;
			tech.elytraTechBoost(ElytraBoostType.LookDirection);
			ClientPlayNetworking.send(ExampleMod.MID_AIR_BOOST_PACKET_ID, PacketByteBufs.empty());
		}
	}
}
