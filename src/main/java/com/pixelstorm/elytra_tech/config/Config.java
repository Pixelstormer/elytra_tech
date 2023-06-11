package com.pixelstorm.elytra_tech.config;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config(name = "elytra_tech")
public class Config implements ConfigData, AutoCloseable {
	// The file that this config was loaded from, and will be saved to. May be null
	@ConfigEntry.Gui.Excluded
	public transient FileConfig backingFile;

	// Configuration values for elytra boosting
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public Boosting boosting;

	public Config() {
		backingFile = null;
	}

	public Config(FileConfig backingFile) {
		this.backingFile = backingFile;
	}

	public void save() {
		if (backingFile != null) {
			new ObjectConverter().toConfig(this, backingFile);
			backingFile.save();
		}
	}

	@Override
	public void close() {
		if (backingFile != null) {
			backingFile.close();
		}
	}

	@Override
	public String toString() {
		return String.format("Config { boosting = %s }", this.boosting);
	}

	public static class Boosting {
		// Whether or not elytra boosting is enabled
		@ConfigEntry.Gui.Tooltip
		public boolean enabled;

		// Amount that is added to velocity by boosting
		@ConfigEntry.Gui.Tooltip
		public double speed;

		// Minimum time between each boost, measured in ticks (1/20ths of a second)
		@ConfigEntry.Gui.Tooltip
		public int cooldown;

		@Override
		public String toString() {
			return String.format("Boosting { enabled = %b, speed = %f, cooldown = %d }", enabled, speed, cooldown);
		}
	}
}
