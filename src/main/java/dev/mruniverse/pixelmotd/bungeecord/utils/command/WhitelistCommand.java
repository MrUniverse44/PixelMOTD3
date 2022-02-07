package dev.mruniverse.pixelmotd.bungeecord.utils.command;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Converter;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitelistCommand {
    private final PixelMOTDBuilder plugin;
    private final String cmdPrefix;

    public WhitelistCommand(PixelMOTDBuilder plugin, String command) {
        this.plugin = plugin;
        this.cmdPrefix = "&8» &a/" + command;
    }

    public void usage(CommandSender sender, String[] arguments) {
        if (arguments.length == 0 || arguments[0].equalsIgnoreCase("help")) {
            help(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("add")) {
            if (arguments.length >= 2) {
                String value = arguments[1];
                PlayerType type = PlayerType.fromUnknown(value);
                String server;
                if (arguments.length == 3) {
                    server = arguments[2];
                } else {
                    server = "global";
                }
                List<String> list;
                String path;
                if (type == PlayerType.PLAYER) {
                    path = "players." + server + ".by-name";
                } else {
                    path = "players." + server + ".by-uuid";
                }
                list = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStringList(path);
                if (list.contains(value)) {
                    sendMessage(
                            sender,
                            plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString(
                                            "messages.already-whitelisted",
                                            "&a<type> &e<player> &ais already in the whitelist!"
                                    )
                                    .replace("<type>",type.getName())
                                    .replace("<player>",value)
                    );
                    return;
                }
                list.add(value);
                sendMessage(
                        sender,
                        plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString(
                                        "messages.whitelist-player-add",
                                        "&a<type> &e<player> &ahas been &badded &ato the whitelist."
                                )
                                .replace("<type>",type.getName())
                                .replace("<player>",value)
                );
                plugin.getWhitelist().update(plugin);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set(path,list);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).save();
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).reload();
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("on")) {
            if (arguments.length >= 2) {
                String author;
                if (sender instanceof ProxiedPlayer) {
                    author = sender.getName();
                } else {
                    author = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getString("settings.console-name","Console");
                }
                String server = arguments[1];
                String reason;
                if (arguments.length >= 3) {
                    reason = Converter.ListToStringText(
                            Arrays.asList(getArguments(arguments))
                    );
                } else {
                    reason = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getString("settings.default-reason","The server will be updated!");
                }
                if (!plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).contains("whitelist." + server + ".kick-message")) {
                    List<String> defaultKick = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStringList("settings.default-kick-message");
                    Map<String,String> replacements = new HashMap<>();
                    if (server.equalsIgnoreCase("global")) {
                        replacements.put("%server%", server);
                    } else {
                        replacements.put("%server%", "the Network");
                    }
                    defaultKick = Converter.listReplacer(defaultKick,replacements);
                    plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".kick-message",defaultKick);
                }
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".reason",reason);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".Enabled",true);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".author",author);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).save();
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).reload();
                plugin.getPing().setWhitelist(true);
                sendMessage(sender,plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString("messages.whitelist-enabled"));
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("off")) {
            if (arguments.length >= 2) {
                String author;
                if (sender instanceof ProxiedPlayer) {
                    author = sender.getName();
                } else {
                    author = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getString("settings.console-name","Console");
                }
                String server = arguments[1];
                String reason;
                if (arguments.length >= 3) {
                    reason = Converter.ListToStringText(
                            Arrays.asList(getArguments(arguments))
                    );
                } else {
                    reason = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getString("settings.default-reason","The server will be updated!");
                }
                if (!plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).contains("whitelist." + server + ".kick-message")) {
                    List<String> defaultKick = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStringList("settings.default-kick-message");
                    plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".kick-message",defaultKick);
                }
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".reason",reason);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".Enabled",false);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set("whitelist." + server + ".author",author);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).save();
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).reload();
                plugin.getPing().setWhitelist(false);
                sendMessage(sender,plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString("messages.whitelist-disabled"));
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("remove")) {
            if (arguments.length >= 2) {
                String value = arguments[1];
                PlayerType type = PlayerType.fromUnknown(value);
                String server;
                if (arguments.length == 3) {
                    server = arguments[2];
                } else {
                    server = "global";
                }
                List<String> list;
                String path;
                if (type == PlayerType.PLAYER) {
                    path = "players." + server + ".by-name";
                } else {
                    path = "players." + server + ".by-uuid";
                }
                list = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStringList(path);
                if (!list.contains(value)) {
                    sendMessage(
                            sender,
                            plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString(
                                            "messages.not-whitelisted",
                                            "&a<type> &e<player> &ais not in the whitelist!"
                                    )
                                    .replace("<type>",type.getName())
                                    .replace("<player>",value)
                    );
                    return;
                }
                list.remove(value);
                sendMessage(
                        sender,
                        plugin.getStorage().getFiles().getControl(GuardianFiles.MESSAGES).getColoredString(
                                        "messages.whitelist-player-remove",
                                        "&a<type> &e<player> &ahas been&b removed &afrom the whitelist."
                                )
                                .replace("<type>",type.getName())
                                .replace("<player>",value)
                );
                plugin.getWhitelist().update(plugin);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).set(path,list);
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).save();
                plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).reload();
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("list")) {
            sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
            Control control = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
            for(String key : control.getContent("players",false)) {
                sendList(sender,control,key);
            }
            sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
            return;
        }
        help(sender);
    }

    private void sendList(CommandSender sender,Control control,String key) {
        String player = "players." + key + ".by-name";
        String uuid =  "players." + key + ".by-uuid";
        List<String> players = control.getStringList(player);
        List<String> uuids = control.getStringList(uuid);
        sendMessage(sender,"&8" + key + " players: (&7" + players.size() + "&8)");
        for(String value : players) {
            sendMessage(sender,"  &8- &7" + value);
        }
        sendMessage(sender,"&8" + key + " uuids: (&7" + players.size() + "&8)");
        for(String value : uuids) {
            sendMessage(sender,"  &8- &7" + value);
        }
    }

    private void help(CommandSender sender) {
        space(sender);
        sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
        sendMessage(sender,"&8Admin - Whitelist Commands:");
        sendMessage(sender,cmdPrefix + " admin whitelist list &8- &7List of players,uuids in the whitelist");
        sendMessage(sender,cmdPrefix + " admin whitelist add (player) [global/<server|world>] &8- &7Add a player to the whitelist");
        sendMessage(sender,cmdPrefix + " admin whitelist remove (player) [global/<server|world>] &8- &7Remove a player from whitelist");
        sendMessage(sender,cmdPrefix + " admin whitelist on [global/<server|world>] [reason] &8- &7Turn on the Whitelist");
        sendMessage(sender,cmdPrefix + " admin whitelist off [global/<server|world>] [reason] &8- &7Turn off the Whitelist");
        sendMessage(sender,"&a&l────── PIXEL MOTD ──────");
    }

    private void space(CommandSender sender) {
        sender.sendMessage(
                new TextComponent(
                        " "
                )
        );
    }

    private void sendMessage(CommandSender sender,String message) {
        sender.sendMessage(
                new TextComponent(
                        ChatColor.translateAlternateColorCodes('&',message)
                )
        );
    }

    private void argumentsIssue(CommandSender sender) {
        sendMessage(sender,"&7Invalid arguments, please use the correct syntax.");
    }

    private String[] getArguments(String[] args) {
        String[] arguments = new String[args.length - 3];
        int argID = 0;
        int aID = 0;
        for(String arg : args) {
            if (aID < 3) {
                arguments[argID] = arg;
                argID++;
            }
            aID++;
        }
        return arguments;
    }

}
