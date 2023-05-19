package com.pixelstorm.elytra_tech;

public class Config {
	// Amount that is added to velocity by boosting
	public float boostSpeed;

	// Minimum time between each boost, measured in ticks (1/20ths of a second?)
	public int boostCooldown;

	public Config(float speed, int cooldown) {
		this.boostSpeed = speed;
		this.boostCooldown = cooldown;
	}

	@Override
	public String toString() {
		return String.format("Config { boostSpeed = %f, boostCooldown = %d }", this.boostSpeed, this.boostCooldown);
	}
}
