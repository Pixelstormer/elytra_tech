package com.pixelstorm.elytra_tech;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.quiltmc.loader.api.QuiltLoader;

import org.tomlj.Toml;
import org.tomlj.TomlInvalidTypeException;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

public class ConfigLoader {
	public static final String CONFIG_FILENAME = "elytra_tech.toml";
	public static final String DEFAULT_CONFIG_RESOURCE_PATH = "data/default_config.toml";

	public static Path getDefaultConfigPath() {
		return QuiltLoader.getConfigDir().resolve(CONFIG_FILENAME);
	}

	public static Config loadFromDefaultPath() {
		Path configPath = getDefaultConfigPath();
		if (Files.exists(configPath)) {
			try {
				return loadFromPath(configPath);
			} catch (TomlParseError e) {
				Throwable[] suppressed = e.getSuppressed();
				String plurality = suppressed.length == 0 ? "a TOML parsing error" : "TOML parsing errors";
				ElytraTech.LOGGER.error(String.format(
						"Could not load config file from the default location '%s' due to %s! The default config will be loaded instead:",
						configPath, plurality), e);
			} catch (IllegalArgumentException e) {
				ElytraTech.LOGGER.error(String.format(
						"Could not load config file from the default location '%s' as it is malformed! The default config will be loaded instead:",
						configPath), e);
			} catch (IOException e) {
				ElytraTech.LOGGER.error(String.format(
						"Could not load config file from the default location '%s' due to an IO error! The default config will be loaded instead:",
						configPath), e);
			}
			ElytraTech.LOGGER.error(
					"If this error persists, consider manually editing the aforementioned file to fix it, or failing that, deleting the file so that it can be regenerated with the default config.");
			return loadDefaultConfig();
		} else {
			ElytraTech.LOGGER.warn(
					"Could not load config file from the default location '{}' as it does not exist! The default config will be loaded and written to this location instead.",
					configPath);
			try {
				writeDefaultConfig(configPath);
			} catch (IOException e) {
				ElytraTech.LOGGER.error(
						"Could not write default config to the aforementioned location! The default config will still be loaded for this session, but this error may happen again if the issue is not fixed:",
						e);
			}
			return loadDefaultConfig();
		}
	}

	public static Config loadFromPath(Path configPath) throws IOException, TomlParseError, IllegalArgumentException {
		TomlParseResult result;
		result = Toml.parse(configPath);

		if (result.hasErrors()) {
			List<TomlParseError> errors = result.errors();
			TomlParseError firstError = errors.get(0);
			for (TomlParseError e : errors.subList(1, errors.size())) {
				firstError.addSuppressed(e);
			}
			throw firstError;
		} else {
			return loadFromToml(result);
		}
	}

	public static Config loadFromToml(TomlTable toml) throws IllegalArgumentException {
		Double speed;
		String speedKey = "boosting.speed";

		try {
			speed = toml.getDouble(speedKey);
		} catch (TomlInvalidTypeException e) {
			throw new IllegalArgumentException(
					String.format("Key '%s' is required to be of type Double!", speedKey), e);
		}

		if (speed == null) {
			throw new IllegalArgumentException(
					String.format("Required key '%s' of type Double does not have a value set!", speedKey));
		}

		Long cooldown;
		String cooldownKey = "boosting.cooldown";

		try {
			cooldown = toml.getLong(cooldownKey);
		} catch (TomlInvalidTypeException e) {
			throw new IllegalArgumentException(
					String.format("Key '%s' is required to be of type Long!", cooldownKey), e);
		}

		if (cooldown == null) {
			throw new IllegalArgumentException(
					String.format("Required key '%s' of type Long does not have a value set!", cooldownKey));
		}

		return new Config(speed.floatValue(), cooldown.intValue());
	}

	public static InputStream getDefaultConfigBytes() {
		return ElytraTech.CLASSLOADER.getResourceAsStream(DEFAULT_CONFIG_RESOURCE_PATH);
	}

	public static Config loadDefaultConfig() {
		try (InputStream stream = getDefaultConfigBytes();) {
			TomlTable toml = Toml.parse(stream);
			float speed = toml.getDouble("boosting.speed").floatValue();
			int cooldown = toml.getLong("boosting.cooldown").intValue();
			return new Config(speed, cooldown);
		} catch (Exception e) {
			ElytraTech.LOGGER.error("Could not load the default config! This should never happen:", e);
			throw new RuntimeException(e);
		}
	}

	public static void writeDefaultConfig(Path configPath) throws IOException {
		try (InputStream input = getDefaultConfigBytes();) {
			Files.copy(input, configPath);
		}
	}
}
