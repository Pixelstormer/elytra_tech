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

public class ConfigLoader {
	public static final String CONFIG_FILENAME = "elytra_tech.toml";
	public static final URL DEFAULT_CONFIG_RESOURCE_URL = ConfigLoader.class.getClassLoader()
			.getResource("data/default_config.toml");

	public static Config loadFromDefaultPath() throws ConfigLoadingException {
		Path configPath = getDefaultConfigPath();
		return loadFromPath(configPath);
	}

	public static Path getDefaultConfigPath() {
		return QuiltLoader.getConfigDir().resolve(CONFIG_FILENAME);
	}

	public static Config loadFromPath(Path configPath) throws ConfigLoadingException {
		FileConfig fileConfig = prepareFileForLoading(configPath);
		return loadFromFile(fileConfig, new Config(fileConfig));
	}

	public static FileConfig prepareFileForLoading(Path configFilePath) {
		return CommentedFileConfig.builder(configFilePath, TomlFormat.instance())
				.defaultData(DEFAULT_CONFIG_RESOURCE_URL)
				.build();
	}

	public static Config loadFromFile(FileConfig fileConfig, Config destination) throws ConfigLoadingException {
		try {
			fileConfig.load();
			new ObjectConverter().toObject(fileConfig, destination);
			return destination;
		} catch (IllegalArgumentException | ParsingException e) {
			throw new ConfigLoadingException(e);
		}
	}

	public static Config loadDefaultConfig() {
		Path configPath;
		try {
			configPath = Path.of(DEFAULT_CONFIG_RESOURCE_URL.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException("Class loader returned an invalid URI! This should never happen.", e);
		}

		try (FileConfig fileConfig = prepareFileForLoading(configPath)) {
			return loadFromFile(fileConfig, new Config());
		} catch (ConfigLoadingException e) {
			throw new RuntimeException("Failed to load default config! This should never happen.", e);
		}
	}
}
