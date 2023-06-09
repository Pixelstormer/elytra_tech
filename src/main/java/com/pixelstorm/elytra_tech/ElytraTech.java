package com.pixelstorm.elytra_tech;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelstorm.elytra_tech.config.Config;
import com.pixelstorm.elytra_tech.config.ConfigLoader;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ElytraTech implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Elytra Tech");

	public static final Identifier ELYTRA_BOOST_PACKET_ID = new Identifier("elytra_tech", "elytra.boost");

	public static final Identifier ELYTRA_FLAP_SOUND_ID = new Identifier("elytra_tech", "elytra.flap");
	public static final SoundEvent ELYTRA_FLAP_SOUND_EVENT = SoundEvent.createVariableRangeEvent(ELYTRA_FLAP_SOUND_ID);

	public static Config config;

	@Override
	public void onInitialize(ModContainer mod) {
		config = ConfigLoader.loadFromDefaultPath();
		LOGGER.info("Loaded config:");
		LOGGER.info(config.toString());

		Registry.register(Registries.SOUND_EVENT, ELYTRA_FLAP_SOUND_ID, ELYTRA_FLAP_SOUND_EVENT);

		ServerPlayConnectionEvents.INIT.register((handler, server) -> {
			ServerPlayNetworking.registerReceiver(handler, ELYTRA_BOOST_PACKET_ID,
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
