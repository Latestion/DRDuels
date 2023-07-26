package dev.latestion.drduels.commands;

import dev.latestion.drduels.LatestDuels;
import dev.latestion.drduels.arena.ArenaManager;
import dev.latestion.drduels.game.Queue;
import dev.latestion.drduels.kits.Kit;
import dev.latestion.drduels.kits.KitManager;
import dev.latestion.drduels.module.Profile;
import dev.latestion.drduels.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandManager implements CommandExecutor {

    public CommandManager() {

        Objects.requireNonNull(Bukkit.getPluginCommand("stats")).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("duel")).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("accept")).setExecutor(this);

    }

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

        if (label.equalsIgnoreCase("duel")) {

            if (args.length == 0) {
                player.sendMessage("Invalid format. Usage: /duel <player> [kit]");
                return true;
            }

            if (Queue.getInvited().containsKey(player)) {
                int x = 60;
                player.sendMessage(ChatColor.RED + "You have already invited someone, wait for " + x + " minutes to duel someone else!");
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

            if (duel.equals(player)) {
                player.sendMessage(ChatColor.RED + "You cannot duel yourself!");
                return true;
            }

            KitManager manager = LatestDuels.getInstance().getKitManager();
            Kit kit = manager.getDefaultKit();

            if (args.length > 1) {
                String kitname = args[1];
                kit = LatestDuels.getInstance().getKitManager().getKitByName(kitname);
            }

            Queue.handle(player, duel, kit);

        }
        else if (label.equalsIgnoreCase("accept")) {

            if (args.length == 0) {
                player.sendMessage("Invalid format. Usage: /accept <player>");
                return true;
            }

            Player p = Bukkit.getPlayerExact(args[0]);

            if (p == null) {
                player.sendMessage(ChatColor.RED + "Invalid Player " + args[0]);
                return true;
            }

            Pair<Player, Kit> duel = Queue.getInvited().get(p);

            if (duel == null || !duel.a().equals(player)) {
                player.sendMessage("You were not invited to duel!");
                return true;
            }

            ArenaManager.handle(player, p, duel.b());
            p.sendMessage(player.getName() + " has accepted your request!");

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
