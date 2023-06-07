package com.pixelstorm.elytra_tech;

public interface CanFreeLook {
	void changeFreeLookDirection(double cursorDeltaX, double cursorDeltaY);

	void setFreeLookPitch(float pitch);

	float getFreeLookPitch();

	void setFreeLookYaw(float yaw);

	float getFreeLookYaw();

	void setFreeLooking(boolean isFreeLooking);

	boolean isFreeLooking();
}
