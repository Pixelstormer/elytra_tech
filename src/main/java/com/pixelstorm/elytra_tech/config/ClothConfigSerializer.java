package com.pixelstorm.elytra_tech.config;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import me.shedaniel.autoconfig.serializer.ConfigSerializer;

@ClientOnly
public class ClothConfigSerializer implements ConfigSerializer<Config> {
	public Config config;

	public ClothConfigSerializer(Config config) {
		this.config = config;
	}

	@Override
	public void serialize(Config config) throws SerializationException {
		config.save();
	}

	@Override
	public Config deserialize() throws SerializationException {
		return config;
	}

	@Override
	public Config createDefault() {
		return ConfigLoader.loadDefaultConfig();
	}
}
