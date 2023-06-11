package com.pixelstorm.elytra_tech;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.pixelstorm.elytra_tech.config.Config;
import com.pixelstorm.elytra_tech.config.ConfigLoadingException;
import com.pixelstorm.elytra_tech.config.MalformedConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;

@ClientOnly
public class ElytraTechModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ConfigLoadingException exception = ElytraTechClient.clothConfigLoadingException;
			if (exception == null) {
				return AutoConfig.getConfigScreen(Config.class, parent).get();
			} else {
				return new MalformedConfigScreen(parent, exception);
			}
		};
	}
}
