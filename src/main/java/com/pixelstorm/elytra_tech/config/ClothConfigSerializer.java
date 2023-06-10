package com.pixelstorm.elytra_tech.config;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import me.shedaniel.autoconfig.serializer.ConfigSerializer;

@ClientOnly
public class ClothConfigSerializer implements ConfigSerializer<Config> {
	@Override
	public void serialize(Config config) throws SerializationException {
		config.save();
	}

	@Override
	public Config deserialize() throws SerializationException {
		return ConfigLoader.loadFromDefaultPath();
	}

	@Override
	public Config createDefault() {
		return ConfigLoader.loadDefaultConfig();
	}
}
