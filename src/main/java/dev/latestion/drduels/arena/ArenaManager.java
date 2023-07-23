package dev.latestion.drduels.arena;

import dev.latestion.drduels.LatestDuels;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class ArenaManager {

    private Location one, two;
    @Getter private final World world;

    public ArenaManager() {

        FileConfiguration config = LatestDuels.getInstance().getArenaData().getConfig();

        String worldName = config.getString("world", "world");
        world = Bukkit.getWorld(worldName);

        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Invalid World!");
            return;
        }


        one = parse(config.getDouble("player_one_spawn.x"),
                config.getDouble("player_one_spawn.y"),
                config.getDouble("player_one_spawn.z"), world);

        two = parse(config.getDouble("player_two_spawn.x"),
                config.getDouble("player_two_spawn.y"),
                config.getDouble("player_two_spawn.z"), world);

    }

    public Location getPlayerSpawnOne() {
        return one.clone();
    }

    public Location getPlayerSpawnTwo() {
        return two.clone();
    }

    private Location parse(double x, double y, double z, World world) {
        return new Location(world, x, y, z);
    }

}
