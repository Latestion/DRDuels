package dev.latestion.drduels.module;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Profile {

    @Getter private final UUID id;

    @Getter private long wins, loss, kills, deaths, streak;

    public Profile(UUID id) {
        this.id = id;
    }

    public Document serialize() {
        Document doc = new Document("id", id.toString());
        doc.append("wins", wins).append("loss", loss).append("kills", kills).append("deaths", deaths).append("streak", streak);
        return null;
    }

    public static Profile handle(Document doc) {
        return new Profile(UUID.fromString(doc.getString("id")),
                doc.getLong("wins"), doc.getLong("loss"), doc.getLong("kills"),
                doc.getLong("deaths"), doc.getLong("streak"));
    }

}
