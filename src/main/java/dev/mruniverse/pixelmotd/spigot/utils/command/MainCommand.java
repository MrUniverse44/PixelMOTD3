package dev.mruniverse.pixelmotd.spigot.utils.command;


import dev.mruniverse.pixelmotd.global.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.utils.command.sub.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final PixelMOTD plugin;
    private final String cmdPrefix;
    private final WhitelistCommand whitelist;
    private final BlacklistCommand blacklist;

    public MainCommand(PixelMOTD plugin, String command) {
        this.plugin = plugin;
        this.cmdPrefix = "&e/" + command;
        blacklist = new BlacklistCommand(plugin,command);
        whitelist = new WhitelistCommand(plugin,command);
    }

    public static void sendMessage(Player player,String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        player.sendMessage(message);
    }
    public static void sendMessage(CommandSender sender,String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        sender.sendMessage(message);
    }

    private boolean hasPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean check = true;
        if(sender instanceof Player) {
            Player player = (Player)sender;
            check = player.hasPermission(permission);
            if(sendMessage) {
                String permissionMsg = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString("messages.others.no-perms");
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
                sender.sendMessage(" ");
                sendMessage(sender,"&b------------ &aPixelMOTD &b------------");
                sendMessage(sender,"&7&oCreated by MrUniverse44 w/ help from Sebastnchan & SUPREMObenjamin");
                if(hasPermission(sender,"pmotd.admin.help",false)) sendMessage(sender,cmdPrefix + " admin &e- &fAdmin commands");
                sendMessage(sender,"&b------------ &aPixelMOTD &b------------");
                return true;
            }
            if (args[0].equalsIgnoreCase("admin")) {
                if(args.length == 1 || args[1].equalsIgnoreCase("1")) {
                    if (hasPermission(sender, "pmotd.admin.help.game", true)) {
                        sender.sendMessage(" ");
                        sendMessage(sender, "&b------------ &aPixelMOTD &b------------");
                        sendMessage(sender, cmdPrefix + " admin whitelist add [player or uuid] &e- &fAdd player to whitelist.");
                        sendMessage(sender, cmdPrefix + " admin whitelist remove [player or uuid] &e- &fRemove player from whitelist.");
                        sendMessage(sender, cmdPrefix + " admin blacklist add [player or uuid] &e- &fAdd player to whitelist.");
                        sendMessage(sender, cmdPrefix + " admin blacklist remove [player or uuid] &e- &fRemove player from whitelist.");
                        sendMessage(sender, cmdPrefix + " admin reload &e- &fReload the plugin.");
                        sendMessage(sender, "&b------------ &a(Page 1&l/1&a) &b------------");
                    }
                    return true;
                }

                if(args[1].equalsIgnoreCase("reload")) {
                    if(hasPermission(sender,"pmotd.admin.use.reload",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        long timeMS = System.currentTimeMillis();
                        try {
                            plugin.getLoader().getMotdListener().update();
                            plugin.getListener().update(plugin);

                            if(plugin.getStorage().getControl(GuardianFiles.SETTINGS).getBoolean("settings.update-file-storage",true)) {
                                plugin.getStorage().updateFileManager();
                            } else {
                                plugin.getStorage().reloadFile(FileSaveMode.ALL);
                            }

                            String lang = plugin.getStorage().getControl(GuardianFiles.SETTINGS).getString("settings.language","en");

                            plugin.getLoader().update();

                            plugin.getStorage().setMessages(lang);

                        }catch (Throwable throwable) {
                            plugin.getLogs().error("Something bad happened, maybe the plugin is broken, please check if you have all without issues");
                            plugin.getLogs().error("If you are sure than this isn't your error, please contact the developer.");
                            plugin.getLogs().error(throwable);
                        }
                        String reload = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString("messages.reload","&aThe plugin was reloaded correctly in <ms>ms.");
                        reload = reload.replace("<ms>", (System.currentTimeMillis() - timeMS) + "");
                        sendMessage(sender,reload);
                    }
                    return true;
                }

                if(args[1].equalsIgnoreCase("whitelist")) {
                    if(hasPermission(sender,"pmotd.admin.help.whitelist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        whitelist.usage(sender,getArguments(args));

                    }
                    return true;
                }
                if(args[1].equalsIgnoreCase("blacklist")) {
                    if(hasPermission(sender,"pmotd.admin.help.blacklist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        blacklist.usage(sender,getArguments(args));

                    }
                    return true;
                }
            }
            return true;
        } catch (Throwable throwable) {
            plugin.getLogs().error(throwable);
        }
        return true;
    }
    private String[] getArguments(String[] args){
        String[] arguments = new String[args.length - 2];
        int argID = 0;
        int aID = 0;
        for(String arg : args) {
            if(aID != 0 && aID != 1) {
                arguments[argID] = arg;
                argID++;
            }
            aID++;
        }
        return arguments;
    }
}


