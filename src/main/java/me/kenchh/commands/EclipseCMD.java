package me.kenchh.commands;

import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.fails.FailProfile;
import me.kenchh.checks.fails.FailProfileManager;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import org.bukkit.Bukkit;
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
                p.sendMessage(Eclipse.prefix + "Eclipse AntiCheat v" + getClass().getPackage().getImplementationVersion());
                p.sendMessage(Eclipse.prefix + "Type " + ChatColor.GOLD + "/eclipse help" + ChatColor.YELLOW + " for help");
                return true;
            }

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("help")) {
                    help(p);
                    return true;
                }

                if(args[0].equalsIgnoreCase("alerts")) {
                    DataProfileManager.getDataProfile(p).alerts = !DataProfileManager.getDataProfile(p).alerts;
                    if(DataProfileManager.getDataProfile(p).alerts) {
                        p.sendMessage(Eclipse.prefix + "Alerts " + ChatColor.GREEN + "on.");
                        return true;
                    }
                    p.sendMessage(Eclipse.prefix + "Alerts " + ChatColor.RED + "off.");
                }

                if(args[0].equalsIgnoreCase("checkVL") || args[0].equalsIgnoreCase("check")) {
                    p.sendMessage(Eclipse.prefix + ChatColor.RED + "Usage: /eclipse check <player>");
                    return true;
                }

                if(args[0].equalsIgnoreCase("switchCheckMode") || args[0].equalsIgnoreCase("setCheckMode")) {
                    p.sendMessage(Eclipse.prefix + ChatColor.RED + "Usage: /eclipse setCheckMode <player> <checkmode>");
                    return true;
                }

                if(args[0].equalsIgnoreCase("debug")) {
                    Eclipse.getInstance().debug = !Eclipse.getInstance().debug;
                    return true;
                }

                if(args[0].equalsIgnoreCase("ignoreCheck")) {
                    p.sendMessage(ChatColor.RED  + "Usage: /eclipse ignorecheck <player> <check> <seconds>");
                    return true;
                }

            }

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("help")) {
                    help(p);
                    return true;
                }

                if(args[0].equalsIgnoreCase("checkVL") || args[0].equalsIgnoreCase("check")) {
                    Player ps = Bukkit.getPlayer(args[1]);

                    if(ps.isOnline() == true && ps != null) {
                        FailProfile fp = FailProfileManager.getFailProfile(ps);
                        p.sendMessage(ChatColor.YELLOW + "======[ " + ChatColor.GOLD + ps.getName() + "'s Failprofile" + ChatColor.YELLOW + " ]======");
                        for(Check c : CheckManager.checks) {
                            if(fp != null) {
                                p.sendMessage(ChatColor.RED + "" + fp.getVL(c) + ChatColor.GOLD + " - " + c.name);
                            } else {
                                p.sendMessage(ChatColor.RED + "0" + ChatColor.GOLD + " - " + c.name);
                            }
                        }
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("switchCheckMode") || args[0].equalsIgnoreCase("setCheckMode")) {
                    p.sendMessage(Eclipse.prefix + ChatColor.RED + "Usage: /eclipse setCheckMode <player> <checkmode>");
                    return true;
                }

                if(args[0].equalsIgnoreCase("checkdebug")) {
                    String checkname = args[1];

                    Check c = CheckManager.getCheck(checkname);

                    if(c != null) {
                        c.checkdebug = !c.checkdebug;
                        p.sendMessage(Eclipse.prefix + "Check Debug mode:" + ChatColor.GOLD + " " + c.name + " - " + c.checkdebug);
                    } else {
                        p.sendMessage(Eclipse.prefix + "Please enter a valid check to debug.");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("ignoreCheck")) {
                    p.sendMessage(ChatColor.RED  + "Usage: /eclipse ignorecheck <player> <check> <seconds>");
                    return true;
                }
            }

            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("switchCheckMode") || args[0].equalsIgnoreCase("setCheckMode")) {
                    Player ps = Bukkit.getPlayer(args[1]);

                    if(ps != null) {
                        DataProfile dp = DataProfileManager.getDataProfile(ps);
                        dp.currentCheckMode = CheckManager.getCheckModeByName(args[2]);
                        if(dp.currentCheckMode == null) {
                            p.sendMessage(Eclipse.prefix + ChatColor.GOLD + args[1] + ChatColor.YELLOW + "'s checkmode has been set to " + ChatColor.GOLD + "Vanilla.");
                        } else {
                            p.sendMessage(Eclipse.prefix + ChatColor.GOLD + args[1] + ChatColor.YELLOW + "'s checkmode has been set to " + ChatColor.GOLD + dp.currentCheckMode.name + ".");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "That player is not online!");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("ignoreCheck")) {
                    p.sendMessage(ChatColor.RED  + "Usage: /eclipse ignorecheck <player> <check> <seconds>");
                    return true;
                }

            }

            if(args.length >= 4) {

                if(args[0].equalsIgnoreCase("ignoreCheck")) {
                    Player ps = Bukkit.getPlayer(args[1]);
                    double seconds = Integer.parseInt(args[3]);
                    if(ps != null) {
                        Eclipse.getInstance().api.ignoreCheck(p, args[2], seconds);
                        p.sendMessage(Eclipse.prefix + "Ignoring now "
                                + ChatColor.GOLD + CheckManager.getCheck(args[2]).name
                                + ChatColor.YELLOW + " for " + ChatColor.GOLD + ps.getName()
                                + ChatColor.YELLOW + " for "
                                + ChatColor.GOLD + seconds + " seconds.");
                    } else {
                        p.sendMessage(ChatColor.RED + "That player is not online!");
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public void help(Player p) {
        p.sendMessage(Eclipse.prefix + ChatColor.RED + "/eclipse alerts " + ChatColor.YELLOW + "- Toggle your alerts.");
        p.sendMessage(Eclipse.prefix + ChatColor.RED + "/eclipse debug" + ChatColor.YELLOW + "- Toggle receiving debug-data when failing a check.");
        p.sendMessage(Eclipse.prefix + ChatColor.RED + "/eclipse check <player> " + ChatColor.YELLOW + "- Lookup a players VL for all checks.");
        p.sendMessage(Eclipse.prefix + ChatColor.RED + "/eclipse setCheckMode <player> <check> " + ChatColor.YELLOW + "- Switch the check-mode for a player.");
        p.sendMessage(Eclipse.prefix + ChatColor.RED + "/eclipse ignorecheck <player> <check> <duration> " + ChatColor.YELLOW + "- Make a player ignore a specific check.");
    }

}
