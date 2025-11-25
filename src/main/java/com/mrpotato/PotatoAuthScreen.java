package com.mrpotato;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class PotatoAuthScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget passwordField;
    private TextFieldWidget serverField;
    private ButtonWidget autoLoginButton;
    private ButtonWidget serverModeButton;

    public PotatoAuthScreen(Screen parent) {
        super(Text.literal("PotatoAuth Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        passwordField = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 60, 200, 20, Text.literal("Password"));
        passwordField.setMaxLength(128);
        passwordField.setText(ConfigManager.getPassword());
        this.addSelectableChild(passwordField);

        autoLoginButton = ButtonWidget.builder(
            Text.literal("Auto Login: " + (ConfigManager.isAutoLoginEnabled() ? "ON" : "OFF")),
            button -> {
                boolean newState = !ConfigManager.isAutoLoginEnabled();
                ConfigManager.setAutoLoginEnabled(newState);
                button.setMessage(Text.literal("Auto Login: " + (newState ? "ON" : "OFF")));
            }
        ).dimensions(centerX - 100, centerY - 30, 200, 20).build();
        this.addDrawableChild(autoLoginButton);

        serverModeButton = ButtonWidget.builder(
            Text.literal("Mode: " + (ConfigManager.isSpecificServerMode() ? "Specific Server" : "All Servers")),
            button -> {
                boolean newState = !ConfigManager.isSpecificServerMode();
                ConfigManager.setSpecificServerMode(newState);
                button.setMessage(Text.literal("Mode: " + (newState ? "Specific Server" : "All Servers")));
                updateServerFieldVisibility();
            }
        ).dimensions(centerX - 100, centerY, 200, 20).build();
        this.addDrawableChild(serverModeButton);

        serverField = new TextFieldWidget(this.textRenderer, centerX - 100, centerY + 30, 200, 20, Text.literal("Server"));
        serverField.setMaxLength(256);
        serverField.setText(ConfigManager.getServerHostname());
        updateServerFieldVisibility();
        this.addSelectableChild(serverField);

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Save & Close"),
            button -> {
                ConfigManager.setPassword(passwordField.getText());
                ConfigManager.setServerHostname(serverField.getText());
                this.close();
            }
        ).dimensions(centerX - 100, centerY + 65, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Cancel"),
            button -> this.close()
        ).dimensions(centerX - 100, centerY + 90, 200, 20).build());
    }

    private void updateServerFieldVisibility() {
        if (serverField != null) {
            serverField.setVisible(ConfigManager.isSpecificServerMode());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Password:", this.width / 2 - 100, this.height / 2 - 75, 0xFFFFFF);
        
        if (ConfigManager.isSpecificServerMode()) {
            context.drawTextWithShadow(this.textRenderer, "Server Address:", this.width / 2 - 100, this.height / 2 + 15, 0xFFFFFF);
        }
        
        passwordField.render(context, mouseX, mouseY, delta);
        if (ConfigManager.isSpecificServerMode()) {
            serverField.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
