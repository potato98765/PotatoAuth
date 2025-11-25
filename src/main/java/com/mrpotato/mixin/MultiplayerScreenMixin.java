package com.mrpotato.mixin;

import com.mrpotato.PotatoAuthScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addPotatoAuthButton(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("PotatoAuth"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new PotatoAuthScreen(this));
                }
            }
        ).dimensions(this.width / 2 + 4 + 76 + 80, this.height - 28, 74, 20).build());
    }
}