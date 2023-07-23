package dev.latestion.drduels.utils;

import dev.latestion.drduels.LatestDuels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;


public class DataManager {

    private FileConfiguration dataConfig;
    private File configFile;

    private final String name;

    public DataManager(String name) {
        this.name = name;
        this.dataConfig = null;
        this.configFile = null;
        this.saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(LatestDuels.getInstance().getDataFolder(), name + ".yml");
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defaultStream = LatestDuels.getInstance().getResource(name + ".yml");
        if (defaultStream != null) {
            final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }

    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            this.reloadConfig();
        }
        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            LatestDuels.getInstance().getLogger().log(Level.SEVERE, "Could Not Save Config to" + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(LatestDuels.getInstance().getDataFolder(), name + ".yml");
        }
        if (!this.configFile.exists()) {
            LatestDuels.getInstance().saveResource(name + ".yml", false);
        }
    }

}
