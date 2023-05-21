package com.pixelstorm.elytra_tech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
			return loadFromPath(configPath);
		} else {
			ElytraTech.LOGGER.warn(
					"Could not load config file from the default location '{}' as it does not exist! The default config will be loaded and written to this location instead.",
					configPath);
			try {
				writeDefaultConfig(configPath);
			} catch (IOException e) {
				ElytraTech.LOGGER.error(
						"Could not write default config to the aforementioned location! The default config will still be loaded for this session, but this error may happen again if the issue is not fixed:");
				ElytraTech.LOGGER.error(getPrintedStackTrace(e));

			}
			return loadDefaultConfig();
		}
	}

	public static Config loadFromPath(Path configPath) {
		TomlParseResult result;
		try {
			result = Toml.parse(configPath);
		} catch (IOException e) {
			ElytraTech.LOGGER.error(
					"Could not load config file '{}' due to an IO error! The default config will be loaded instead:",
					configPath);
			ElytraTech.LOGGER.error(getPrintedStackTrace(e));
			return loadDefaultConfig();
		}

		if (result.hasErrors()) {
			ElytraTech.LOGGER.error(
					"Could not load config file '{}' due to TOML parsing errors! The default config will be loaded instead:",
					configPath);
			for (TomlParseError error : result.errors()) {
				ElytraTech.LOGGER.error(getPrintedStackTrace(error));
			}
			return loadDefaultConfig();
		} else {
			try {
				return loadFromToml(result);
			} catch (IllegalArgumentException e) {
				ElytraTech.LOGGER.error(
						"Could not load config file '{}' as it is malformed! The default config will be loaded instead:",
						configPath);
				ElytraTech.LOGGER.error(getPrintedStackTrace(e));
				return loadDefaultConfig();
			}
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
		try {
			InputStream stream = getDefaultConfigBytes();
			TomlTable toml = Toml.parse(stream);
			float speed = toml.getDouble("boosting.speed").floatValue();
			int cooldown = toml.getLong("boosting.cooldown").intValue();
			return new Config(speed, cooldown);
		} catch (Exception e) {
			ElytraTech.LOGGER.error("Could not load the default config! This should never happen:");
			ElytraTech.LOGGER.error(getPrintedStackTrace(e));
			throw new RuntimeException(e);
		}
	}

	public static void writeDefaultConfig(Path configPath) throws IOException {
		// Use CREATE_NEW instead of the default of CREATE to avoid overwriting an
		// existing file
		try (InputStream input = getDefaultConfigBytes();
				OutputStream output = Files.newOutputStream(configPath, StandardOpenOption.CREATE_NEW,
						StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);) {
			input.transferTo(output);
		}
	}

	/**
	 * Collects the output of {@link Throwable#printStackTrace()} to a
	 * {@link String}, because there is no built-in method to do this.
	 *
	 * @param e The throwable to get the stack trace of
	 * @return The printed stack trace of the given throwable
	 */
	public static String getPrintedStackTrace(Throwable e) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(byteStream)) {
			e.printStackTrace(printStream);
		}
		return byteStream.toString(StandardCharsets.UTF_8);
	}
}
