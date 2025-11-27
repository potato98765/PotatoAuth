package com.mrpotato;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.Screen;

public class VersionHelper {
    private static Boolean is121OrNewer = null;
    
    public static boolean is121OrNewer() {
        if (is121OrNewer == null) {
            String version = SharedConstants.getGameVersion().getName();
            is121OrNewer = version.startsWith("1.21") || 
                          version.startsWith("1.22");
        }
        return is121OrNewer;
    }
    
    public static Screen createPotatoAuthScreen(Screen parent) {
        if (is121OrNewer()) {
            return new PotatoAuthScreen(parent);
        } else {
            return new PotatoAuthScreenFallback(parent);
        }
    }
}
