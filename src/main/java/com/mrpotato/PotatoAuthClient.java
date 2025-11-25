package com.mrpotato;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerAddress;

public class PotatoAuthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (ConfigManager.isAutoLoginEnabled() && ConfigManager.getPassword() != null && !ConfigManager.getPassword().isEmpty()) {
                boolean shouldLogin = false;
                
                if (ConfigManager.isSpecificServerMode()) {
                    String targetServer = ConfigManager.getServerHostname();
                    if (targetServer != null && !targetServer.isEmpty()) {
                        String currentServer = getCurrentServerAddress(client);
                        if (currentServer != null && currentServer.equalsIgnoreCase(targetServer)) {
                            shouldLogin = true;
                        }
                    }
                } else {
                    shouldLogin = true;
                }
                
                if (shouldLogin) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            MinecraftClient.getInstance().execute(() -> {
                                if (client.player != null) {
                                    client.player.networkHandler.sendCommand("login " + ConfigManager.getPassword());
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }
    
    private String getCurrentServerAddress(MinecraftClient client) {
        if (client.getCurrentServerEntry() != null) {
            return client.getCurrentServerEntry().address;
        }
        return null;
    }
}