package com.pixelstorm.elytra_tech;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.util.Identifier;

public class ElytraTech implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Elytra Tech");

	public static final Identifier BOOST_PACKET_ID = new Identifier("elytra_tech", "boost");

	@Override
	public void onInitialize(ModContainer mod) {
		ServerPlayConnectionEvents.INIT.register((handler, server) -> {
			ServerPlayNetworking.registerReceiver(handler, BOOST_PACKET_ID,
					ElytraTech::receiveBoostPacket);
		});
	}

	private static void receiveBoostPacket(MinecraftServer server, ServerPlayerEntity player,
			ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			if (player.isFallFlying()) {
				ElytraBooster booster = ((HasElytraBooster) player).getElytraBooster();
				booster.boost();
			}
		});
	}
}
