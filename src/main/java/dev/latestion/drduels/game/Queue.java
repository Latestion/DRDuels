package dev.latestion.drduels.game;

import dev.latestion.drduels.kits.Kit;
import dev.latestion.drduels.utils.Pair;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Queue implements Listener {

    @Getter private static Map<Player, Pair<Player, Kit>> invited = new HashMap<>();

    public static void handle(Player invitee, Player invitedPlayer, Kit kit) {
        invited.put(invitee, Pair.of(invitedPlayer, kit));
        invitedPlayer.sendMessage(ChatColor.GOLD + invitee.getName() + " has challenged you to a duel!");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Iterator<Map.Entry<Player, Pair<Player, Kit>>> iterator = invited.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Player, Pair<Player, Kit>> entry = iterator.next();
            Player p = entry.getKey();
            Pair<Player, Kit> pair = entry.getValue();

            if (p.equals(player)) {
                iterator.remove();
                if (pair == null) return;
                pair.a().sendMessage("Duel request by " + player.getName() + " has expired!");
            } else if (pair.a().equals(player)) {
                iterator.remove();
                player.sendMessage("Duel request by " + pair.a().getName() + " has expired!");
            }
        }
    }

}
