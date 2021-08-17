package dev.mruniverse.pixelmotd.bungeecord.utils.command.sub;

import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.MainCommand;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.PlayerType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlacklistCommand {
    private final PixelMOTD plugin;
    private final String command;
    private final ListType currentType = ListType.BLACKLIST;

    public BlacklistCommand(PixelMOTD plugin, String command) {
        this.plugin = plugin;
        this.command = command;
    }

    public void usage(CommandSender sender, @NotNull String[] arguments) {
        Configuration file = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
        Configuration msg = plugin.getStorage().getControl(GuardianFiles.MESSAGES);
        if (arguments.length == 0 || arguments[0].equalsIgnoreCase("help")) {
            MainCommand.sendMessage(sender," ");
            MainCommand.sendMessage(sender, "&b------------ &aPixelMOTD &b------------");
            MainCommand.sendMessage(sender, "&e/" + command + " admin blacklist add [player or uuid] &e- &fAdd player to whitelist.");
            MainCommand.sendMessage(sender, "&e/" + command + " admin blacklist remove [player or uuid] &e- &fRemove player from whitelist.");
            MainCommand.sendMessage(sender, "&e/" + command + " admin blacklist toggle &e- &fToggle blacklist.");
            MainCommand.sendMessage(sender, "&b------------ &a(Page 1&l/1&a) &b------------");
            return;
        }
        if (arguments[0].equalsIgnoreCase("add")) {
            if (arguments.length == 2) {
                String user = arguments[1];
                PlayerType playerType = PlayerType.fromUnknown(user);
                playerType.setPlayer(user);
                String path = playerType.getListPath().getPath();
                List<String> users = file.getStringList(currentType.getPath() + path);
                if(!users.contains(user)) {
                    users.add(user);
                    plugin.getStorage().getControl(GuardianFiles.WHITELIST).set(currentType.getPath() + path,users);
                    plugin.getStorage().save(FileSaveMode.WHITELIST);
                    plugin.getStorage().reloadFile(FileSaveMode.WHITELIST);
                    plugin.getListener().update(plugin);
                    String message = msg.getString(MessagePath.BLACKLIST_PLAYER_ADD.getPath(),"&a<type> &e<player> &ahas been&b added &ato the blacklist.");
                    MainCommand.sendMessage(sender,messageReplace(message,playerType,currentType));
                    return;
                }
                playerIssue(sender,playerType,currentType,true);
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("remove")) {
            if (arguments.length == 2) {
                String user = arguments[1];
                PlayerType playerType = PlayerType.fromUnknown(user);
                playerType.setPlayer(user);
                String path = playerType.getListPath().getPath();
                List<String> users = file.getStringList(currentType.getPath() + path);
                if(users.contains(user)) {
                    users.remove(user);
                    plugin.getStorage().getControl(GuardianFiles.WHITELIST).set(currentType.getPath() + path,users);
                    plugin.getStorage().save(FileSaveMode.WHITELIST);
                    plugin.getStorage().reloadFile(FileSaveMode.WHITELIST);
                    String message = msg.getString(MessagePath.BLACKLIST_PLAYER_REMOVE.getPath(),"&a<type> &e<player> &ahas been&b removed &afrom the blacklist.");
                    MainCommand.sendMessage(sender,messageReplace(message,playerType,currentType));
                    plugin.getListener().update(plugin);
                    return;
                }
                playerIssue(sender,playerType,currentType,false);
                return;
            }
            argumentsIssue(sender);
            return;
        }
        if (arguments[0].equalsIgnoreCase("toggle")) {
            String path = currentType.getPath() + ListPath.TOGGLE.getPath();
            boolean value = plugin.getStorage().getControl(GuardianFiles.WHITELIST).getBoolean(path);
            boolean finalValue = !value;
            plugin.getStorage().getControl(GuardianFiles.WHITELIST).set(path,finalValue);
            plugin.getStorage().save(FileSaveMode.WHITELIST);
            plugin.getStorage().reloadFile(FileSaveMode.WHITELIST);
            plugin.getListener().update(plugin);
            if(finalValue) {
                MainCommand.sendMessage(sender,msg.getString(MessagePath.BLACKLIST_TOGGLE_ON.getPath(),"&aThe blacklist has been &b&lENABLED&a."));
                return;
            }
            MainCommand.sendMessage(sender,msg.getString(MessagePath.BLACKLIST_TOGGLE_OFF.getPath(),"&aThe blacklist has been &b&lDISABLED&a."));
            return;
        }
        argumentsIssue(sender);
    }

    private void argumentsIssue(CommandSender sender) {
        MainCommand.sendMessage(sender,"&aInvalid arguments, please use &b/" + command + " admin &ato see all commands.");
    }

    public void playerIssue(CommandSender sender, PlayerType type, ListType listType, boolean isAdding) {
        String message;
        if(isAdding) {
            message = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString(MessagePath.ALREADY_BLACKLISTED.getPath(),"&a<type> &e<player> &ais already in the blacklist!");
        } else {
            message = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString(MessagePath.NOT_BLACKLISTED.getPath(),"&a<type> &e<player> &ais not in the blacklist!");
        }
        MainCommand.sendMessage(sender, messageReplace(message,type,listType));
    }
    public String messageReplace(String message,PlayerType type,ListType listType) {
        return message.replace("<type>",type.getName())
                .replace("<player>",type.getValue())
                .replace("<list>", listType.getName());
    }
}
