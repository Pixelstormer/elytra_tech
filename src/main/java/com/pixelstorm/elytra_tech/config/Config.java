package com.pixelstorm.elytra_tech.config;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.FileConfig;

public class Config {
	public static final ConfigSpec SPEC;

	static {
		SPEC = new ConfigSpec();
		SPEC.define("boosting.speed", 0.8);
		SPEC.define("boosting.cooldown", 20);
	}

	// The file that this config was loaded from, and will be saved to
	private FileConfig backingFile;

	// Amount that is added to velocity by boosting
	private double boostSpeed;

	// Minimum time between each boost, measured in ticks (1/20ths of a second?)
	private int boostCooldown;

	public Config(FileConfig config) {
		this.backingFile = config;
		this.boostSpeed = config.get("boosting.speed");
		this.boostCooldown = config.get("boosting.cooldown");
	}

	public FileConfig getBackingFile() {
		return backingFile;
	}

	public void setBackingFile(FileConfig backingFile) {
		this.backingFile = backingFile;
	}

	public double getBoostSpeed() {
		return boostSpeed;
	}

	public void setBoostSpeed(double boostSpeed) {
		this.boostSpeed = boostSpeed;
		this.backingFile.set("boosting.speed", boostSpeed);
	}

	public int getBoostCooldown() {
		return boostCooldown;
	}

	public void setBoostCooldown(int boostCooldown) {
		this.boostCooldown = boostCooldown;
		this.backingFile.set("boosting.cooldown", boostCooldown);
	}

	@Override
	public String toString() {
		return String.format("Config { boostSpeed = %f, boostCooldown = %d }", this.boostSpeed, this.boostCooldown);
	}
}
