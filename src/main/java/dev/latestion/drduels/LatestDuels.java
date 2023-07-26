package dev.latestion.drduels;

import com.mongodb.client.MongoCollection;
import dev.latestion.drduels.arena.ArenaManager;
import dev.latestion.drduels.commands.CommandManager;
import dev.latestion.drduels.kits.KitManager;
import dev.latestion.drduels.module.Profile;
import dev.latestion.drduels.utils.DataManager;
import dev.latestion.drduels.utils.MongoDBManager;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LatestDuels extends JavaPlugin implements Listener {

    @Getter private static LatestDuels instance;

    @Getter private DataManager kitsData, arenaData;
    @Getter private KitManager kitManager;
    @Getter private static ArenaManager arenaManager;

    private static final Map<UUID, Profile> profile = new HashMap<>();
    private MongoDBManager mongo;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.saveDefaultConfig();
        kitsData = new DataManager("kit");
        arenaData = new DataManager("arena");

        kitManager = new KitManager();
        arenaManager = new ArenaManager();

        mongo = new MongoDBManager();
        loadFromMongo();

        PluginManager manager = this.getServer().getPluginManager();

        manager.registerEvents(this, this);
        new CommandManager();
    }

    @Override
    public void onDisable() {
        saveToMongo();
        profile.clear();
    }

    private void loadFromMongo() {
        mongo.getCollection("profiles").find().forEach(doc -> {
            Profile p = Profile.handle(doc);
            profile.put(p.getId(), p);
        });
    }

    private void saveToMongo() {
        MongoCollection<Document> collection = mongo.getCollection("profiles");
        for (Profile p : profile.values()) {
            Document filter = new Document("id", p.getId().toString());
            Document doc = p.serialize();
            if (collection.findOneAndReplace(filter, doc) == null) collection.insertOne(doc);
        }
    }

    public static Profile getProfile(UUID id) {
        return profile.get(id);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        profile.putIfAbsent(id, new Profile(id));
    }

}
