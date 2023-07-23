package dev.latestion.drduels.kits;

import dev.latestion.drduels.LatestDuels;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class KitManager {

    private Map<String, Kit> kitMap = new HashMap<>();
    @Getter private Kit defaultKit;

    public KitManager() {
        FileConfiguration config = LatestDuels.getInstance().getKitsData().getConfig();

        if (!config.contains("kits")) {
            Bukkit.getLogger().log(Level.SEVERE, "No Kits Configed!");
            return;
        }

        config.getConfigurationSection("kits").getKeys(false).forEach(key -> {

            String name = config.getString("kits." + key + ".name");
            // TODO

        });

        if (kitMap.size() == 0) {
            Bukkit.getLogger().log(Level.SEVERE, "No Kits Registered!");
            return;
        }

        String kitName = config.getString("default_kit", "").toLowerCase();
        defaultKit = kitMap.get(kitName);

        if (defaultKit == null) {
            defaultKit = kitMap.entrySet().stream().findFirst().get().getValue();
            Bukkit.getLogger().log(Level.SEVERE, "Invalid Default Kit! Changing default kit to " + defaultKit.getName());
        }

    }

    public Kit getKitByName(String name) {
        return kitMap.getOrDefault(name, defaultKit);
    }
}
