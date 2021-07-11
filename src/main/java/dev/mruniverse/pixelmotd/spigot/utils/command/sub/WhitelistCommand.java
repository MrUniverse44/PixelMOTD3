package dev.mruniverse.pixelmotd.spigot.utils.command.sub;

import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.utils.command.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WhitelistCommand {
    private final PixelMOTD plugin;
    private final String command;
    private final ListType currentType = ListType.WHITELIST;

    public WhitelistCommand(PixelMOTD plugin, String command) {
        this.plugin = plugin;
        this.command = command;
    }

    public void usage(CommandSender sender, @NotNull String[] arguments) {
        FileConfiguration file = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
        FileConfiguration msg = plugin.getStorage().getControl(GuardianFiles.MESSAGES);
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
                    String message = msg.getString(MessagePath.WHITELIST_PLAYER_ADD.getPath(),"&a<type> &e<player> &ahas been&b added &ato the whitelist.");
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
                    String message = msg.getString(MessagePath.WHITELIST_PLAYER_REMOVE.getPath(),"&a<type> &e<player> &ahas been&b removed &afrom the whitelist.");
                    MainCommand.sendMessage(sender,messageReplace(message,playerType,currentType));
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
            if(finalValue) {
                MainCommand.sendMessage(sender,msg.getString(MessagePath.WHITELIST_TOGGLE_ON.getPath(),"&aThe whitelist has been &b&lENABLED&a."));
                return;
            }
            MainCommand.sendMessage(sender,msg.getString(MessagePath.WHITELIST_TOGGLE_OFF.getPath(),"&aThe whitelist has been &b&lDISABLED&a."));
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
            message = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString(MessagePath.ALREADY_WHITELISTED.getPath(),"&a<type> &e<player> &ais already in the whitelist!");
        } else {
            message = plugin.getStorage().getControl(GuardianFiles.MESSAGES).getString(MessagePath.NOT_WHITELISTED.getPath(),"&a<type> &e<player> &ais not in the whitelist!");
        }
        MainCommand.sendMessage(sender, messageReplace(message,type,listType));
    }
    public String messageReplace(String message,PlayerType type,ListType listType) {
        return message.replace("<type>",type.getName())
                .replace("<player>",type.getValue())
                .replace("<list>", listType.getName());
    }
}
