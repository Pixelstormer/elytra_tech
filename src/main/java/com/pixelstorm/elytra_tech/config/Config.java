package com.pixelstorm.elytra_tech.config;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;

public class Config {
	// The file that this config was loaded from, and will be saved to
	public transient FileConfig backingFile;

	// Configuration values for elytra boosting
	public Boosting boosting;

	public Config(FileConfig config) {
		this.backingFile = config;
	}

	public void save() {
		new ObjectConverter().toConfig(this, backingFile);
		backingFile.save();
	}

	@Override
	public String toString() {
		return String.format("Config { boosting = %s }", this.boosting);
	}

	public static class Boosting {
		// Amount that is added to velocity by boosting
		public double speed;

		// Minimum time between each boost, measured in ticks (1/20ths of a second?)
		public int cooldown;

		@Override
		public String toString() {
			return String.format("Boosting { speed = %f, cooldown = %d }", this.speed, this.cooldown);
		}
	}
}
