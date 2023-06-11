package com.pixelstorm.elytra_tech;

import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;

import com.pixelstorm.elytra_tech.config.ClothConfigSerializer;
import com.pixelstorm.elytra_tech.config.Config;
import com.pixelstorm.elytra_tech.config.ConfigLoader;
import com.pixelstorm.elytra_tech.config.ConfigLoadingException;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;

@ClientOnly
public class ElytraTechClient implements ClientModInitializer {
	public static final KeyBind ELYTRA_FREE_LOOK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBind("key.elytra_tech.elytra.freelook", GLFW.GLFW_KEY_BACKSLASH,
					"key.categories.elytra_tech"));

	// The error that caused the config to fail to load.
	// Is null if the config was loaded successfully
	public static ConfigLoadingException clothConfigLoadingException;

	@Override
	public void onInitializeClient(ModContainer mod) {
		loadClothConfig();

		ClientLifecycleEvents.STOPPED.register(client -> {
			if (clothConfigLoadingException == null) {
				AutoConfig.getConfigHolder(Config.class).get().close();
			}
			ElytraTech.config.close();
		});
	}

	private static void loadClothConfig() {
		Config config;
		try {
			config = ConfigLoader.loadFromDefaultPath();
		} catch (ConfigLoadingException e) {
			clothConfigLoadingException = e;
			return;
		}

		clothConfigLoadingException = null;
		AutoConfig.register(Config.class, (annotation, configClass) -> new ClothConfigSerializer(config));
	}
}
