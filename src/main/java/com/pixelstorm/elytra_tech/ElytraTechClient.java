package com.pixelstorm.elytra_tech;

import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;

public class ElytraTechClient implements ClientModInitializer {
	public static final KeyBind ELYTRA_FREE_LOOK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBind("key.elytra_tech.elytra.freelook", GLFW.GLFW_KEY_BACKSLASH,
					"key.categories.elytra_tech"));

	@Override
	public void onInitializeClient(ModContainer mod) {
		ClientLifecycleEvents.STOPPED.register(client -> {
			ElytraTech.config.backingFile.close();
		});
	}
}
