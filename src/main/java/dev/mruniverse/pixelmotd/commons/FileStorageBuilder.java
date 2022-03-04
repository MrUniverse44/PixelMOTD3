package dev.mruniverse.pixelmotd.commons;

import dev.mruniverse.pixelmotd.commons.enums.*;
import dev.mruniverse.pixelmotd.commons.shared.BungeeControl;
import dev.mruniverse.pixelmotd.commons.shared.SpigotControl;
import dev.mruniverse.pixelmotd.commons.shared.VelocityControl;

import java.io.File;
import java.util.HashMap;

public class FileStorageBuilder implements FileStorage {

    private final HashMap<GuardianFiles, Control> files = new HashMap<>();

    private final HashMap<IconFolders, File> folders = new HashMap<>();

    private final GLogger logs;

    private final File dataFolder;

    private File translationsFolder;

    private final InputManager inputManager;

    private final InitialMode initialMode;

    private final boolean isBungee;

    public FileStorageBuilder(GLogger logs, InitialMode mode, File dataFolder, InputManager inputManager) {
        this.dataFolder = dataFolder;
        this.initialMode = mode;
        this.logs = logs;
        this.inputManager = inputManager;
        this.isBungee = inputManager.isBungee();
        load();
        loadIconFolder();
        checkIconFolder();
        checkPluginFolders();
    }

    private void load() {
        for(GuardianFiles guardianFiles : GuardianFiles.values()) {
            File mainFolder = dataFolder;
            if (guardianFiles.isInDifferentFolder()) {
                mainFolder = new File(dataFolder,guardianFiles.getFolderName());
            }
            if (isBungee) {
                files.put(guardianFiles, new BungeeControl(logs,
                        new File(mainFolder,guardianFiles.getFileName()),
                        inputManager.getInputStream(guardianFiles.getFileName())
                ));
            } else {
                if (initialMode.equals(InitialMode.SPIGOT)) {
                    files.put(guardianFiles, new SpigotControl(logs,
                            new File(mainFolder, guardianFiles.getFileName()),
                            inputManager.getInputStream(guardianFiles.getFileName())
                    ));
                } else if (initialMode.equals(InitialMode.VELOCITY)) {
                    files.put(guardianFiles, new VelocityControl(logs,
                            new File(mainFolder, guardianFiles.getFileName()),
                            inputManager.getInputStream(guardianFiles.getFileName())
                    ));
                }
            }
        }
    }

    @Override
    public File getIconsFolder(MotdType motdType) {
        return folders.get(motdType.getIconFolders());
    }

    @Override
    public File getMainIcons() {
        return folders.get(IconFolders.GENERAL);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public File getIconsFolder(MotdType motdType, String motdName) {
        File mainFolder = folders.get(IconFolders.GENERAL);
        File iconFolder = new File(mainFolder,motdType.getName() + "-" + motdName);
        if (!iconFolder.exists()) iconFolder.mkdir();
        return iconFolder;
    }

    private void loadIconFolder() {
        folders.put(IconFolders.GENERAL, new File(dataFolder,IconFolders.GENERAL.getName()));
        File general = folders.get(IconFolders.GENERAL);
        for(IconFolders folder : IconFolders.values()) {
            if (folder != IconFolders.GENERAL) {
                folders.put(folder, new File(general,folder.getName()));
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void checkIconFolder() {
        for (File folders : folders.values()) {
            if (!folders.exists()) folders.mkdir();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void checkPluginFolders() {
        File modesFolder = new File(dataFolder,"modes");
        File translationsFolder = new File(dataFolder,"translations");
        if (!modesFolder.exists()) modesFolder.mkdirs();
        if (!translationsFolder.exists()) translationsFolder.mkdirs();
        this.translationsFolder = translationsFolder;
    }

    @Override
    public File getFile(GuardianFiles fileToGet) {
        return files.get(fileToGet).getFile();
    }

    @Override
    public Control getControl(GuardianFiles file) {
        return files.get(file);
    }

    @Override
    public void setMessages(String code) {
        try {
            checkPluginFolders();
            if (code.equalsIgnoreCase("en") || code.equalsIgnoreCase("english")) {
                logs.info("Plugin now is using language-file code: '" + code + "'.");
                files.put(GuardianFiles.MESSAGES,files.get(GuardianFiles.MESSAGES_EN));
                return;
            }
            if (code.equalsIgnoreCase("es") || code.equalsIgnoreCase("spanish")) {
                logs.info("Plugin now is using language-file code: '" + code + "'.");
                files.put(GuardianFiles.MESSAGES,files.get(GuardianFiles.MESSAGES_ES));
                return;
            }
            if (code.equalsIgnoreCase("pl") || code.equalsIgnoreCase("polish")) {
                logs.info("Plugin now is using language-file code: '" + code + "'.");
                files.put(GuardianFiles.MESSAGES,files.get(GuardianFiles.MESSAGES_PL));
                return;
            }
            if (code.equalsIgnoreCase("jp") || code.equalsIgnoreCase("japanese")) {
                logs.info("Plugin now is using language-file code: '" + code + "'.");
                files.put(GuardianFiles.MESSAGES,files.get(GuardianFiles.MESSAGES_JP));
                return;
            }
            logs.info("Plugin now is using custom language-file code: '" + code + "'.");
            files.put(GuardianFiles.MESSAGES, new SpigotControl(logs,
                    new File(translationsFolder, "messages_" + code + ".yml"))
            );
        }catch (Exception exception) {
            logs.error("Can't find messages file with code: " + code);
            logs.error(exception);
        }
    }

    @Override
    public void reloadFile(FileSaveMode mode) {
        switch (mode) {
            case EMERGENCY:
                files.get(GuardianFiles.EMERGENCY).reload();
                break;
            case EVENTS:
                files.get(GuardianFiles.EVENTS).reload();
                break;
            case MOTDS:
                files.get(GuardianFiles.MOTDS).reload();
                break;
            case WHITELIST:
                files.get(GuardianFiles.WHITELIST).reload();
                break;
            case MESSAGES:
                files.get(GuardianFiles.MESSAGES).reload();
                break;
            case MESSAGES_EN:
                files.get(GuardianFiles.MESSAGES_EN).reload();
                break;
            case MESSAGES_ES:
                files.get(GuardianFiles.MESSAGES_ES).reload();
                break;
            case MESSAGES_PL:
                files.get(GuardianFiles.MESSAGES_PL).save();
            case MESSAGES_JP:
                files.get(GuardianFiles.MESSAGES_JP).reload();
                break;
            case SETTINGS:
                files.get(GuardianFiles.SETTINGS).reload();
                break;
            case ALL:
            default:
                for(GuardianFiles file : GuardianFiles.values()) {
                    files.get(file).reload();
                }
                break;
        }
    }

    @Override
    public void save(FileSaveMode mode) {
        switch (mode) {
            case EMERGENCY:
                files.get(GuardianFiles.EMERGENCY).save();
                break;
            case EVENTS:
                files.get(GuardianFiles.EVENTS).save();
                break;
            case MOTDS:
                files.get(GuardianFiles.MOTDS).save();
                break;
            case WHITELIST:
                files.get(GuardianFiles.WHITELIST).save();
                break;
            case MESSAGES:
                files.get(GuardianFiles.MESSAGES).save();
                break;
            case MESSAGES_EN:
                files.get(GuardianFiles.MESSAGES_EN).save();
                break;
            case MESSAGES_PL:
                files.get(GuardianFiles.MESSAGES_PL).save();
                break;
            case MESSAGES_ES:
                files.get(GuardianFiles.MESSAGES_ES).save();
                break;
            case MESSAGES_JP:
                files.get(GuardianFiles.MESSAGES_JP).save();
                break;
            case SETTINGS:
                files.get(GuardianFiles.SETTINGS).save();
                break;
            case ALL:
            default:
                for(GuardianFiles file : GuardianFiles.values()) {
                    files.get(file).save();
                }
                break;
        }
    }
}
