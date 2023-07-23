package dev.latestion.drduels.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.latestion.drduels.LatestDuels;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.Objects;

public class MongoDBManager {

    private MongoDatabase database;

    public MongoDBManager() {
        try {
            MongoClient client = MongoClients.create(Objects.requireNonNull(LatestDuels.getInstance().getConfig()
                    .getString("mongo.client_address")));
            database = client.getDatabase("LatestDuels");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ChatColor.RED + "COULD NOT CONNECT TO MongoDB, Disabling plugin!");
            LatestDuels.getInstance().getServer().getPluginManager().disablePlugin(LatestDuels.getInstance());
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection(String cell) {
        return getDatabase().getCollection(cell);
    }

}
