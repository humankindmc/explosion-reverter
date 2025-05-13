package me.rileycalhoun.explosionreverter.command;

import me.rileycalhoun.explosionreverter.ExplosionReverter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ExplosionReverterCommand implements CommandExecutor {

    private final ExplosionReverter plugin;

    public ExplosionReverterCommand(ExplosionReverter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if(args.length == 0) {
            sendInfo(sender);
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "ExplosionReverter has been reloaded.");
            } else {
                sendInfo(sender);
            }
        }

        return true;
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage(
                ChatColor.GREEN + "ExplosionReverter version "
                + plugin.getDescription().getVersion()
        );
    }

}
