package dev.mruniverse.pixelmotd.spigot.storage;

import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.motd.MotdType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FileStorage {

    private final PixelMOTD plugin;

    private FileConfiguration settings;
    private FileConfiguration messagesEn;
    private FileConfiguration messagesEs;
    private FileConfiguration messages;
    private FileConfiguration motds;
    private FileConfiguration whitelist;
    private FileConfiguration events;

    private final File rxSettings;
    private final File rxMotds;
    private final File rxEvents;
    private final File rxWhitelist;
    private final File rxMessagesEn;
    private final File rxMessagesEs;
    private final File iconsFolder;
    private final File normalFolder;
    private final File whitelistFolder;
    private final File outdatedClientFolder;
    private final File outdatedServerFolder;


    private File rxMessages;

    public FileStorage(PixelMOTD plugin) {
        this.plugin = plugin;
        File dataFolder = plugin.getDataFolder();
        iconsFolder = new File(dataFolder, "icons");
        normalFolder = new File(iconsFolder, "normal");
        rxMotds = new File(dataFolder,"motds.yml");
        rxEvents = new File(dataFolder,"events.yml");
        rxWhitelist = new File(dataFolder, "whitelist.yml");
        whitelistFolder = new File(iconsFolder, "whitelist");
        outdatedClientFolder = new File(iconsFolder, "outdatedClient");
        outdatedServerFolder = new File(iconsFolder, "outdatedServer");
        rxSettings = new File(dataFolder, "settings.yml");
        rxMessages = new File(dataFolder, "messages_en.yml");
        rxMessagesEn = new File(dataFolder, "messages_en.yml");
        rxMessagesEs = new File(dataFolder, "messages_es.yml");
        settings = loadConfig("settings");
        messagesEn = loadConfig("messages_en");
        messagesEs = loadConfig("messages_es");
        events = loadConfig("events");
        motds = loadConfig("motds");
        whitelist = loadConfig("whitelist");
        messages = loadConfig("messages_es");
        checkIconFolder();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkIconFolder() {
        if(!iconsFolder.exists()) iconsFolder.mkdir();
        if(!normalFolder.exists()) normalFolder.mkdir();
        if(!whitelistFolder.exists()) whitelistFolder.mkdir();
        if(!outdatedClientFolder.exists()) outdatedClientFolder.mkdir();
        if(!outdatedServerFolder.exists()) outdatedServerFolder.mkdir();
    }

    public File getIconsFolder(MotdType motdType) {
        checkIconFolder();
        switch (motdType) {
            case WHITELIST:
                return whitelistFolder;
            case OUTDATED_CLIENT:
                return outdatedClientFolder;
            case OUTDATED_SERVER:
                return outdatedServerFolder;
            default:
            case NORMAL:
                return normalFolder;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getIconsFolder(MotdType motdType, String motdName) {
        File iconFolder = new File(iconsFolder,motdType.getName() + "-" + motdName);
        if(!iconFolder.exists()) iconFolder.mkdir();
        return iconFolder;
    }

    public void setMessages(String code) {
        if(code.equalsIgnoreCase("en")) {
            rxMessages = rxMessagesEn;
            messages = messagesEn;
            return;
        }
        if(code.equalsIgnoreCase("es")) {
            rxMessages = rxMessagesEs;
            messages = messagesEs;
            return;
        }
        rxMessages = new File(plugin.getDataFolder(),"messages_" + code + ".yml");
        messages = loadConfig("messages_" + code);
    }

    public File getFile(GuardianFiles fileToGet) {
        switch (fileToGet) {
            case EVENTS:
                return rxEvents;
            case MESSAGES:
                return rxMessages;
            case MESSAGES_ES:
                return rxMessagesEs;
            case WHITELIST:
                return rxWhitelist;
            case MOTDS:
                return rxMotds;
            case MESSAGES_EN:
                return rxMessagesEn;
            case SETTINGS:
            default:
                return rxSettings;
        }
    }

    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param configName config to create/reload.
     */
    public FileConfiguration loadConfig(String configName) {
        File configFile = new File(plugin.getDataFolder(), configName + ".yml");

        if (!configFile.exists()) {
            saveConfig(configName);
        }

        FileConfiguration cnf = null;
        try {
            cnf = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogs().warn(String.format("A error occurred while loading the settings file. Error: %s", e));
            e.printStackTrace();
        }

        plugin.getLogs().info(String.format("&7File &e%s.yml &7has been loaded", configName));
        return cnf;
    }
    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param rigoxFile config to create/reload.
     */
    public FileConfiguration loadConfig(File rigoxFile) {
        if (!rigoxFile.exists()) {
            saveConfig(rigoxFile);
        }

        FileConfiguration cnf = null;
        try {
            cnf = YamlConfiguration.loadConfiguration(rigoxFile);
        } catch (Exception e) {
            plugin.getLogs().warn(String.format("A error occurred while loading the settings file. Error: %s", e));
            e.printStackTrace();
        }

        plugin.getLogs().info(String.format("&7File &e%s &7has been loaded", rigoxFile.getName()));
        return cnf;
    }

    /**
     * Reload plugin file(s).
     *
     * @param Mode mode of reload.
     */
    public void reloadFile(FileSaveMode Mode) {
        switch (Mode) {
            case EVENTS:
                events = YamlConfiguration.loadConfiguration(rxEvents);
                break;
            case MESSAGES_ES:
                messagesEs = YamlConfiguration.loadConfiguration(rxMessagesEs);
                break;
            case MOTDS:
                motds = YamlConfiguration.loadConfiguration(rxMotds);
                break;
            case WHITELIST:
                whitelist = YamlConfiguration.loadConfiguration(rxWhitelist);
                break;
            case MESSAGES:
                messages = YamlConfiguration.loadConfiguration(rxMessages);
                break;
            case MESSAGES_EN:
                messagesEn = YamlConfiguration.loadConfiguration(rxMessagesEn);
                break;
            case SETTINGS:
                settings = YamlConfiguration.loadConfiguration(rxSettings);
                break;
            case ALL:
            default:
                events = YamlConfiguration.loadConfiguration(rxEvents);
                whitelist = YamlConfiguration.loadConfiguration(rxWhitelist);
                motds = YamlConfiguration.loadConfiguration(rxMotds);
                settings = YamlConfiguration.loadConfiguration(rxSettings);
                messages = YamlConfiguration.loadConfiguration(rxMessages);
                messagesEn = YamlConfiguration.loadConfiguration(rxMessagesEn);
                messagesEs = YamlConfiguration.loadConfiguration(rxMessagesEs);
                break;
        }
    }

    /**
     * Save config File using FileStorage
     *
     * @param fileToSave config to save/create with saveMode.
     */
    public void save(FileSaveMode fileToSave) {
        try {
            switch (fileToSave) {
                case MOTDS:
                    motds.save(rxMotds);
                    break;
                case WHITELIST:
                    whitelist.save(rxWhitelist);
                    break;
                case EVENTS:
                    events.save(rxEvents);
                    break;
                case MESSAGES_ES:
                    messagesEs.save(rxMessagesEs);
                    break;
                case MESSAGES:
                    messages.save(rxMessages);
                    break;
                case MESSAGES_EN:
                    messagesEn.save(rxMessagesEn);
                    break;
                case SETTINGS:
                    settings.save(rxSettings);
                    break;
                case ALL:
                default:
                    events.save(rxEvents);
                    whitelist.save(rxWhitelist);
                    motds.save(rxMotds);
                    settings.save(rxSettings);
                    messages.save(rxMessages);
                    messagesEs.save(rxMessagesEs);
                    messagesEn.save(rxMessagesEn);
                    break;
            }
        } catch (Throwable throwable) {
            plugin.getLogs().error("Can't save a file!");

        }
    }
    /**
     * Save config File Changes & Paths
     *
     * @param configName config to save/create.
     */
    public void saveConfig(String configName) {
        File folderDir = plugin.getDataFolder();
        File file = new File(plugin.getDataFolder(), configName + ".yml");
        if (!folderDir.exists()) {
            boolean createFile = folderDir.mkdir();
            if(createFile) plugin.getLogs().info("&7Folder created!");
        }

        if (!file.exists()) {
            try (InputStream in = plugin.getResource(configName + ".yml")) {
                if(in != null) {
                    Files.copy(in, file.toPath());
                }
            } catch (Throwable throwable) {
                plugin.getLogs().error(String.format("A error occurred while copying the config %s to the plugin data folder. Error: %s", configName, throwable));
                plugin.getLogs().error(throwable);
            }
        }
    }
    /**
     * Save config File Changes & Paths
     *
     * @param fileToSave config to save/create.
     */
    public void saveConfig(File fileToSave) {
        if (!fileToSave.getParentFile().exists()) {
            boolean createFile = fileToSave.mkdir();
            if(createFile) plugin.getLogs().info("&7Folder created!!");
        }

        if (!fileToSave.exists()) {
            plugin.getLogs().debug(fileToSave.getName());
            try (InputStream in = plugin.getResource(fileToSave.getName() + ".yml")) {
                if(in != null) {
                    Files.copy(in, fileToSave.toPath());
                }
            } catch (Throwable throwable) {
                plugin.getLogs().error(String.format("A error occurred while copying the config %s to the plugin data folder. Error: %s", fileToSave.getName(), throwable));
                plugin.getLogs().error(throwable);
            }
        }
    }

    /**
     * Control a file, getControl() will return a FileConfiguration.
     *
     * @param fileToControl config to control.
     */
    public FileConfiguration getControl(GuardianFiles fileToControl) {
        switch (fileToControl) {
            case EVENTS:
                if (events == null) events = loadConfig(rxEvents);
                return events;
            case WHITELIST:
                if (whitelist == null) whitelist = loadConfig(rxWhitelist);
                return whitelist;
            case MOTDS:
                if (motds == null) motds = loadConfig(rxMotds);
                return motds;
            case MESSAGES:
                if (messages == null) messages = loadConfig(rxMessages);
                return messages;
            case MESSAGES_ES:
                if (messagesEs == null) messagesEs = loadConfig(rxMessagesEs);
                return messagesEs;
            case MESSAGES_EN:
                if (messagesEn == null) messagesEn = loadConfig(rxMessagesEn);
                return messagesEn;
            case SETTINGS:
            default:
                if (settings == null) settings = loadConfig(rxSettings);
                return settings;
        }
    }

    public String getString(GuardianFiles file,@NotNull String path) {
        String currentPath = getControl(file).getString(path);
        if(currentPath == null) currentPath = "invalid path";
        return ChatColor.translateAlternateColorCodes('&', currentPath);
    }

    public String getStringWithoutColors(GuardianFiles file,String path) {
        return getControl(file).getString(path);
    }

    public List<String> getColoredList(GuardianFiles file,String path) {
        List<String> coloredList = new ArrayList<>();
        getControl(file).getStringList(path).forEach(text -> {
            coloredList.add(ChatColor.translateAlternateColorCodes('&', text));
        });
        return coloredList;
    }

    public List<String> getContent(GuardianFiles file, String path, boolean getKeys) {
        List<String> rx = new ArrayList<>();
        ConfigurationSection section = getControl(file).getConfigurationSection(path);
        if(section == null) return rx;
        rx.addAll(section.getKeys(getKeys));
        return rx;
    }

}
