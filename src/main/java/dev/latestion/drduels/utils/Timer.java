package dev.latestion.drduels.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private final Player[] players;

    public Timer(Player... players) {
        this.players = players;
    }

    int i = 0;

    @Override
    public void run() {
        i++;
        for (Player p : players) {
            if (p.isOnline())
                p.sendActionBar(Component.text("Total Time: ", Style.style(NamedTextColor.GOLD)).append(Component.text(fancy())));
        }
    }

    private String fancy() {
        int hours = i / 3600;
        int minutes = (i % 3600) / 60;
        int remainingSeconds = i % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

}
