package dev.mruniverse.pixelmotd.bungeecord.utils.command;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MainCommand extends Command {


    private final PixelMOTDBuilder plugin;
    private final String cmdPrefix;

    public MainCommand(PixelMOTDBuilder plugin, String command) {
        super(command);
        this.plugin = plugin;
        this.cmdPrefix = "&e/" + command;
    }

    public static void sendMessage(ProxiedPlayer player, String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        player.sendMessage(new TextComponent(message));
    }
    public static void sendMessage(CommandSender sender, String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        sender.sendMessage(new TextComponent(message));
    }

    private boolean hasPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean check = true;
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)sender;
            check = player.hasPermission(permission);
            if(sendMessage) {
                String permissionMsg = plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString("messages.others.no-perms");
                if (permissionMsg == null) permissionMsg = "&cYou need permission &7%permission% &cfor this action.";
                if (!check)
                    sendMessage(player, permissionMsg.replace("%permission%", permission));
            }
        }
        return check;
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args   arguments used to invoke this command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendMessage(sender," ");
                sendMessage(sender,"&b------------ &7PixelMOTD &b------------");
                sendMessage(sender,"&7&oCreated by MrUniverse44 w/ help from Sebastnchan & SUPREMObenjamin");
                if(hasPermission(sender,"pmotd.admin.help",false)) sendMessage(sender,cmdPrefix + " admin &3- &7Admin commands");
                sendMessage(sender,"&b------------ &7PixelMOTD &b------------");
                return;
            }
            if (args[0].equalsIgnoreCase("admin")) {
                if(args.length == 1 || args[1].equalsIgnoreCase("1")) {
                    if (hasPermission(sender, "pmotd.admin.help.game", true)) {
                        sendMessage(sender," ");
                        sendMessage(sender, "&b------------ &7PixelMOTD &b------------");
                        sendMessage(sender, cmdPrefix + " admin reload &3- &7Reload the plugin.");
                        sendMessage(sender, "&b[] &f= &eOPTIONAL &8| &a() &f= &eOBLIGATORY");
                        sendMessage(sender, "&b------------ &7(Page &31/1&7) &b------------");
                    }
                    return;
                }

                if(args[1].equalsIgnoreCase("reload")) {
                    if(hasPermission(sender,"pmotd.admin.use.reload",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        long timeMS = System.currentTimeMillis();
                        try {
                            plugin.getStorage().getFiles().reloadFile(FileSaveMode.ALL);

                            plugin.getPing().update();

                            String lang = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getString("settings.language","en");

                            plugin.getStorage().getFiles().setMessages(lang);

                            plugin.update(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS));
                            
                        }catch (Throwable throwable) {
                            plugin.getStorage().getLogs().error("Something bad happened, maybe the plugin is broken, please check if you have all without issues");
                            plugin.getStorage().getLogs().error("If you are sure than this isn't your error, please contact the developer.");
                            plugin.getStorage().getLogs().error(throwable);
                        }
                        String reload = plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getString("messages.reload","&aThe plugin was reloaded correctly in <ms>ms.");
                        reload = reload.replace("<ms>", (System.currentTimeMillis() - timeMS) + "");
                        sendMessage(sender,reload);
                    }
                    return;
                }

                if(args[1].equalsIgnoreCase("whitelist")) {
                    if(hasPermission(sender,"pmotd.admin.help.whitelist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        /*
                         * WHITELIST
                         */

                    }
                    return;
                }
                if(args[1].equalsIgnoreCase("blacklist")) {
                    if(hasPermission(sender,"pmotd.admin.help.blacklist",true) || hasPermission(sender,"pmotd.admin.help.*",true)) {
                        /*
                         * BLACKLIST
                         */

                    }
                }
            }
        } catch (Throwable throwable) {
            plugin.getStorage().getLogs().error(throwable);
        }
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
