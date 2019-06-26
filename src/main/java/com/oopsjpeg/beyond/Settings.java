package com.oopsjpeg.beyond;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    public static final String TOKEN = "token";
    public static final String PREFIX = "prefix";
    public static final String MONGO_HOST = "mongo_host";
    public static final String MONGO_DATABASE = "mongo_database";
    private static final Properties DEFAULTS = new Properties();

    static {
        DEFAULTS.put(TOKEN, "");
        DEFAULTS.put(PREFIX, "/");
        DEFAULTS.put(MONGO_HOST, "127.0.0.1");
        DEFAULTS.put(MONGO_DATABASE, "beyond");
    }

    private final String file;
    private final Properties properties = new Properties();

    public Settings(String file) {
        this.file = file;
        properties.putAll(DEFAULTS);
    }

    public String getFile() {
        return file;
    }

    public void load() throws IOException {
        try (FileReader fr = new FileReader(file)) {
            properties.load(fr);
        }
    }

    public void save() throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            properties.store(fw, "gacha settings");
        }
    }

    public String get(String key) {
        return properties.getProperty(key, "");
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public long getLong(String key) {
        return Long.parseLong(get(key));
    }

}
