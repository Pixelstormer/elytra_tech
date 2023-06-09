package com.pixelstorm.elytra_tech.config;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import org.quiltmc.loader.api.QuiltLoader;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.pixelstorm.elytra_tech.ElytraTech;

public class ConfigLoader {
	public static final String CONFIG_FILENAME = "elytra_tech.toml";
	public static final URL DEFAULT_CONFIG_RESOURCE_URL = ConfigLoader.class.getClassLoader()
			.getResource("data/default_config.toml");

	public static Config loadFromDefaultPath() {
		Path configPath = getDefaultConfigPath();
		return loadFromPath(configPath);
	}

	public static Path getDefaultConfigPath() {
		return QuiltLoader.getConfigDir().resolve(CONFIG_FILENAME);
	}

	public static Config loadFromPath(Path configPath) {
		FileConfig fileConfig = loadFileFromPath(configPath);
		return loadFromFile(fileConfig);
	}

	public static FileConfig loadFileFromPath(Path configFilePath) {
		return CommentedFileConfig.builder(configFilePath, TomlFormat.instance())
				.defaultData(DEFAULT_CONFIG_RESOURCE_URL)
				.build();
	}

	public static Config loadFromFile(FileConfig fileConfig) {
		try {
			fileConfig.load();
			return new ObjectConverter().toObject(fileConfig, () -> new Config(fileConfig));
		} catch (IllegalArgumentException | ParsingException e) {
			ElytraTech.LOGGER.error(String.format(
					"Could not load config file '%s' as it is malformed! The default config will be loaded instead:",
					fileConfig.getNioPath()), e);
			return loadDefaultConfig();
		}
	}

	public static Config loadDefaultConfig() {
		Path configPath;
		try {
			configPath = Path.of(DEFAULT_CONFIG_RESOURCE_URL.toURI());
		} catch (URISyntaxException e) {
			ElytraTech.LOGGER.error("Class loader returned an invalid URI! This should never happen:", e);
			throw new RuntimeException(e);
		}
		return loadFromPath(configPath);
	}
}
