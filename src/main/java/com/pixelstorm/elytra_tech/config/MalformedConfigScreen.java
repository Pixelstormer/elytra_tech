package com.pixelstorm.elytra_tech.config;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.pixelstorm.elytra_tech.ElytraTech;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@ClientOnly
public class MalformedConfigScreen extends Screen {
	private final Screen parent;
	private final ConfigLoadingException exception;

	public MalformedConfigScreen(Screen parent, ConfigLoadingException exception) {
		super(Text.translatable("text.autoconfig.elytra_tech.title"));
		this.parent = parent;
		this.exception = exception;
	}

	@Override
	protected void init() {
		ElytraTech.LOGGER.info("Width = {}, height = {}", width, height);
		ButtonWidget closeButton = ButtonWidget.builder(Text.literal("Close"), button -> closeScreen())
				.position((width / 2) - (ButtonWidget.DEFAULT_WIDTH / 2), height - (ButtonWidget.DEFAULT_HEIGHT * 2))
				.build();
		addDrawableChild(closeButton);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, textRenderer, Text.literal("Your config is malformed"), width / 2, (height / 2) - 50,
				0xffffff);
		drawCenteredText(matrices, textRenderer, Text.literal(exception.toString()), width / 2, height / 2,
				0xaa0000);
	}

	@Override
	public void closeScreen() {
		client.setScreen(parent);
	}
}
