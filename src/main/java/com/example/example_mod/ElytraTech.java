package com.example.example_mod;

public interface ElytraTech {
	public enum ElytraBoostType {
		// A boost in the direction the player is moving
		Velocity,
		// A boost in the direction the player is looking
		LookDirection,
		// A boost that doesn't actually add any speed
		Fake
	}

	void tickElytraTech();

	boolean elytraTechBoost(ElytraBoostType type);
}
