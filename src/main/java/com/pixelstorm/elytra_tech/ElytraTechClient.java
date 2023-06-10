package com.pixelstorm.elytra_tech;

import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;

import com.pixelstorm.elytra_tech.config.ClothConfigSerializer;
import com.pixelstorm.elytra_tech.config.Config;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;

@ClientOnly
public class ElytraTechClient implements ClientModInitializer {
	public static final KeyBind ELYTRA_FREE_LOOK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBind("key.elytra_tech.elytra.freelook", GLFW.GLFW_KEY_BACKSLASH,
					"key.categories.elytra_tech"));

	@Override
	public void onInitializeClient(ModContainer mod) {
		AutoConfig.register(Config.class, (annotation, configClass) -> new ClothConfigSerializer());

		ClientLifecycleEvents.STOPPED.register(client -> {
			AutoConfig.getConfigHolder(Config.class).getConfig().close();
			ElytraTech.config.close();
		});
	}
}
