package com.pixelstorm.elytra_tech.mixin.client;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.pixelstorm.elytra_tech.ElytraBooster;
import com.pixelstorm.elytra_tech.ElytraTech;
import com.pixelstorm.elytra_tech.HasElytraBooster;

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
	private void doBoost(CallbackInfo ci) {
		boolean startedJumpingThisTick = this.input.jumping && !this.wasJumpingPreviousTick;
		boolean startedFallFlyingThisTick = this.isFallFlying() && !this.wasFallFlyingPreviousTick;
		if (startedJumpingThisTick && this.isFallFlying() && !startedFallFlyingThisTick) {
			ElytraBooster booster = ((HasElytraBooster) this).getElytraBooster();
			if (booster.boost()) {
				ClientPlayNetworking.send(ElytraTech.BOOST_PACKET_ID, PacketByteBufs.empty());
			}
		}
	}
}
