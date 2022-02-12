package dev.mruniverse.pixelmotd.spigot.utils.command;


import dev.mruniverse.pixelmotd.commons.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final PixelMOTD plugin;

    private final String cmdPrefix;

    private final BlacklistCommand blacklistCommand;

    private final WhitelistCommand whitelistCommand;

    public MainCommand(PixelMOTD plugin, String command) {
        this.plugin = plugin;
        this.cmdPrefix = "&8» &a/" + command;
        blacklistCommand = new BlacklistCommand(plugin,command);
        whitelistCommand = new WhitelistCommand(plugin,command);
    }

    public static void sendMessage(Player player,String message) {
        if (message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        player.sendMessage(message);
    }
    public static void sendMessage(CommandSender sender,String message) {
        if (message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        sender.sendMessage(message);
    }

    private boolean hasPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean check = true;
        if (sender instanceof Player) {
            Player player = (Player)sender;
            check = player.hasPermission(permission);
            if (sendMessage) {
                String permissionMsg = plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString("messages.others.no-perms");
                if (permissionMsg == null) permissionMsg = "&cYou need permission &7%permission% &cfor this action.";
                if (!check)
                    sendMessage(player, permissionMsg.replace("%permission%", permission));
            }
        }
        return check;
    }


    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        try {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendMessage(sender," ");
                sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
                sendMessage(sender,"&8Created by MrUniverse44 w/ help from Sebastnchan & SUPREMObenjamin");
                if (hasPermission(sender,"pmotd.admin.help",false) || hasPermission(sender,"pmotd.admin.help.*",false)) sendMessage(sender,cmdPrefix + " admin &8- &7Admin commands");
                sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
                return true;
            }
            if (args[0].equalsIgnoreCase("admin")) {
                if (args.length == 1 || args[1].equalsIgnoreCase("1")) {
                    if (hasPermission(sender, "pmotd.admin.help", true)) {
                        sendMessage(sender," ");
                        sendMessage(sender, "&a&l────── PIXEL MOTD ──────");
                        sendMessage(sender, cmdPrefix + " admin whitelist &8- &7Whitelist Commands.");
                        sendMessage(sender, cmdPrefix + " admin blacklist &8- &7Blacklist Commands.");
                        sendMessage(sender, cmdPrefix + " admin reload &8- &7Reload the plugin.");
                        sendMessage(sender, "&8[] &f= &bOPTIONAL &8| &8() &f= &bOBLIGATORY");
                        sendMessage(sender, "&a&l────── PIXEL MOTD ──────");
                    }
                    return true;
                }

                if (args[1].equalsIgnoreCase("reload")) {
                    if (hasPermission(sender,"pmotd.admin.use.reload",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        long timeMS = System.currentTimeMillis();
                        try {
                            plugin.getStorage().getFiles().reloadFile(FileSaveMode.ALL);

                            String lang = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getString("settings.language","en");

                            plugin.getStorage().getFiles().setMessages(lang);

                            plugin.getPing().update();

                            plugin.update(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS));

                            plugin.getStorage().updatePriority();

                            plugin.getWhitelist().update(plugin);

                        }catch (Exception exception) {
                            plugin.getStorage().getLogs().error("Something bad happened, maybe the plugin is broken, please check if you have all without issues");
                            plugin.getStorage().getLogs().error("If you are sure than this isn't your error, please contact the developer.");
                            plugin.getStorage().getLogs().error(exception);
                        }
                        String reload = plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString("messages.reload","&aThe plugin was reloaded correctly in <ms>ms.");
                        reload = reload.replace("<ms>", (System.currentTimeMillis() - timeMS) + "");
                        sendMessage(sender,reload);
                    }
                    return true;
                }

                if (args[1].equalsIgnoreCase("whitelist")) {
                    if (hasPermission(sender,"pmotd.admin.help.whitelist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        whitelistCommand.usage(sender,getArguments(args));
                    }
                    return true;
                }
                if (args[1].equalsIgnoreCase("blacklist")) {
                    if (hasPermission(sender,"pmotd.admin.help.blacklist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        blacklistCommand.usage(sender,getArguments(args));
                    }
                }
            }
            return true;
        } catch (Exception exception) {
            plugin.getStorage().getLogs().error(exception);
        }
        return true;
    }
    private String[] getArguments(String[] args) {
        String[] arguments = new String[args.length - 2];
        int argID = 0;
        int aID = 0;
        for(String arg : args) {
            if (aID != 0 && aID != 1) {
                arguments[argID] = arg;
                argID++;
            }
            aID++;
        }
        return arguments;
    }
}


