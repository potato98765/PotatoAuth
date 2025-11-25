package com.mrpotato;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("potatoauth.json");
    private static Config config = new Config();

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, Config.class);
                if (config == null) config = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPassword() {
        if (config.password == null || config.password.isEmpty()) {
            return "";
        }
        return EncryptionUtil.decrypt(config.password);
    }

    public static void setPassword(String password) {
        config.password = EncryptionUtil.encrypt(password);
        save();
    }

    public static boolean isAutoLoginEnabled() {
        return config.autoLoginEnabled;
    }

    public static void setAutoLoginEnabled(boolean enabled) {
        config.autoLoginEnabled = enabled;
        save();
    }

    public static boolean isSpecificServerMode() {
        return config.specificServerMode;
    }

    public static void setSpecificServerMode(boolean enabled) {
        config.specificServerMode = enabled;
        save();
    }

    public static String getServerHostname() {
        return config.serverHostname;
    }

    public static void setServerHostname(String hostname) {
        config.serverHostname = hostname;
        save();
    }

    private static class Config {
        String password = "";
        boolean autoLoginEnabled = false;
        boolean specificServerMode = false;
        String serverHostname = "";
    }
}
