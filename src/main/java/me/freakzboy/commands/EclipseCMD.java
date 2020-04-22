package me.freakzboy.commands;

import me.freakzboy.checks.Check;
import me.freakzboy.checks.CheckManager;
import me.freakzboy.main.Eclipse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EclipseCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("eclipse.eclipse")) {
                return false;
            }

            if(args.length == 0) {
                p.sendMessage(Eclipse.prefix + "by FreakzBoy - Type " + ChatColor.GOLD + "/eclipse help" + ChatColor.YELLOW + " for help");
                return true;
            }

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("debug")) {
                    p.sendMessage(Eclipse.prefix + "Please enter a valid check to debug.");
                    return true;
                }

                p.sendMessage(Eclipse.prefix + "by FreakzBoy - Type " + ChatColor.GOLD + "/eclipse help" + ChatColor.YELLOW + " for help");
            }

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("debug")) {
                    String checkname = args[1];

                    Check c = CheckManager.getCheck(checkname);

                    if(c != null) {
                        c.toggleDebug();
                        p.sendMessage(Eclipse.prefix + "Debug mode:" + ChatColor.GOLD + " " + c.name + " - " + c.debug);
                    } else {
                        p.sendMessage(Eclipse.prefix + "Please enter a valid check to debug.");
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
