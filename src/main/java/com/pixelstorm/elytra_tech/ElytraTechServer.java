package com.pixelstorm.elytra_tech;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;

public class ElytraTechServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer(ModContainer mod) {
		ServerLifecycleEvents.STOPPED.register(server -> {
			ElytraTech.config.backingFile.close();
		});
	}
}
