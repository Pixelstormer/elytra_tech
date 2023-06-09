package com.pixelstorm.elytra_tech.config;

import java.nio.file.Path;

import org.quiltmc.loader.api.QuiltLoader;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.pixelstorm.elytra_tech.ElytraTech;

public class ConfigLoader {
	public static final String CONFIG_FILENAME = "elytra_tech.toml";
	public static final String DEFAULT_CONFIG_RESOURCE_PATH = "data/default_config.toml";

	public static Config loadFromDefaultPath() {
		Path configPath = getDefaultConfigPath();
		return loadFromPath(configPath);
	}

	public static Path getDefaultConfigPath() {
		return QuiltLoader.getConfigDir().resolve(CONFIG_FILENAME);
	}

	public static Config loadFromPath(Path configPath) {
		FileConfig fileConfig = loadWithoutSpecFromPath(configPath);
		applySpec(fileConfig, Config.SPEC);
		return new Config(fileConfig);
	}

	public static FileConfig loadWithoutSpecFromPath(Path configFilePath) {
		FileConfig config = CommentedFileConfig.builder(configFilePath, TomlFormat.instance())
				.autosave()
				.defaultData(ElytraTech.CLASSLOADER.getResource(DEFAULT_CONFIG_RESOURCE_PATH))
				.build();
		config.load();
		return config;
	}

	public static void applySpec(FileConfig config, ConfigSpec spec) {
		if (!spec.isCorrect(config)) {
			ElytraTech.LOGGER.warn(
					"Config file at location '{}' was malformed! See the following corrections for details:",
					config.getNioPath());

			spec.correct(config, (action, path, incorrectValue, correctedValue) -> {
				String pathString = String.join(".", path);
				switch (action) {
					case ADD:
						ElytraTech.LOGGER.warn("Required key '{}' was missing! Populated with default value '{}'.",
								pathString,
								correctedValue);
						break;
					case REMOVE:
						ElytraTech.LOGGER.warn(
								"Found unexpected key '{}' with value '{}'! Removed key from the config.",
								pathString,
								incorrectValue);
						break;
					case REPLACE:
						ElytraTech.LOGGER.warn("Key '{}' had an invalid value '{}'! Replaced with default value '{}'.",
								pathString, incorrectValue, correctedValue);
						break;
				}
			});

			config.save();
		}
	}
}
