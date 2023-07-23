package dev.latestion.drduels.commands;

import dev.latestion.drduels.LatestDuels;
import dev.latestion.drduels.kits.Kit;
import dev.latestion.drduels.kits.KitManager;
import dev.latestion.drduels.module.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player!");
            return false;
        }

        if (label.equalsIgnoreCase("stats")) {
            statsCommand(player, args.length == 0 ? player.getName() : args[0]);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Invalid format. Usage: /duel <player> [kit]");
            return true;
        }

        String playerName = args[0];

        Player duel = Bukkit.getPlayerExact(playerName);

        if (duel == null || !duel.hasPlayedBefore()) {
            player.sendMessage(ChatColor.RED + "Invalid Player " + args[0]);
            return true;
        }

        if (!duel.isOnline()) {
            player.sendMessage(duel.getName() + " is not online!");
            return true;
        }

        KitManager manager = LatestDuels.getInstance().getKitManager();
        Kit kit = manager.getDefaultKit();

        if (args.length > 1) {
            String kitname = args[1];
            kit = LatestDuels.getInstance().getKitManager().getKitByName(kitname);
        }



        return false;
    }

    public void statsCommand(Player player, String name) {

        Profile profile = LatestDuels.getProfile(player.getUniqueId());

        if (!name.equalsIgnoreCase(player.getName())) {

            Player p = Bukkit.getPlayerExact(name);

            if (p == null || !p.hasPlayedBefore()) {
                player.sendMessage(ChatColor.RED + "Invalid Player " + name);
                return;
            }

            if (!p.isOnline()) {
                player.sendMessage(p.getName() + " is not online!");
                return;
            }

            profile = LatestDuels.getProfile(p.getUniqueId());
            name = p.getName();

        }

        player.sendMessage(ChatColor.GOLD + "Info " + name,
                ChatColor.GOLD + "Wins: " + ChatColor.GRAY + profile.getWins(),
                ChatColor.GOLD + "Losses: " + ChatColor.GRAY + profile.getLoss(),
                ChatColor.GOLD + "Kills: " + ChatColor.GRAY + profile.getKills(),
                ChatColor.GOLD + "Deaths: " + ChatColor.GRAY + profile.getDeaths(),
                ChatColor.GOLD + "Win Streak: " + ChatColor.GRAY + profile.getStreak());

    }

}
