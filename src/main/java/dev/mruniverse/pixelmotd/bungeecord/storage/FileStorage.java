package dev.mruniverse.pixelmotd.bungeecord.storage;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.global.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FileStorage {

    private final PixelMOTD plugin;

    private Configuration settings;
    private Configuration messagesEn;
    private Configuration messagesEs;
    private Configuration messages;
    private Configuration motds;
    private Configuration whitelist;
    private Configuration events;

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
        plugin.getLogs().info("Trying to use language code: " + code);
        if(code.equalsIgnoreCase("en")) {
            rxMessages = rxMessagesEn;
            messages = messagesEn;
            plugin.getLogs().info("Plugin now is using language-file English");
            return;
        }
        if(code.equalsIgnoreCase("es")) {
            rxMessages = rxMessagesEs;
            messages = messagesEs;
            plugin.getLogs().info("Plugin now is using language-file Spanish");
            return;
        }
        plugin.getLogs().info("Plugin now is using language-file-code '" + code + "'.");
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
    public Configuration loadConfig(String configName) {
        File configFile = new File(plugin.getDataFolder(), configName + ".yml");

        if (!configFile.exists()) {
            saveConfig(configName);
        }

        Configuration cnf = null;
        try {
            cnf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
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
    public Configuration loadConfig(File rigoxFile) {
        if (!rigoxFile.exists()) {
            saveConfig(rigoxFile);
        }

        Configuration cnf = null;
        try {
            cnf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rigoxFile);
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
        try {
            switch (Mode) {
                case MOTDS:
                    motds = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMotds);
                    break;
                case WHITELIST:
                    whitelist = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxWhitelist);
                    break;
                case EVENTS:
                    events = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxEvents);
                    break;
                case MESSAGES_ES:
                    messagesEs = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessagesEs);
                    break;
                case MESSAGES:
                    messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessages);
                    break;
                case MESSAGES_EN:
                    messagesEn = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessagesEn);
                    break;
                case SETTINGS:
                    settings = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxSettings);
                    break;
                case ALL:
                default:
                    events = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxEvents);
                    whitelist = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxWhitelist);
                    motds = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMotds);
                    settings = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxSettings);
                    messagesEs = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessagesEs);
                    messagesEn = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessagesEn);
                    messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(rxMessages);
                    break;
            }
        }catch (Throwable throwable) {
            plugin.getLogs().error("Unexpected error when the plugin was loading files:");
            plugin.getLogs().error(throwable);
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
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(motds, rxMotds);
                    break;
                case WHITELIST:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(whitelist, rxWhitelist);
                    break;
                case EVENTS:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(events, rxEvents);
                    break;
                case MESSAGES_ES:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messagesEs, rxMessagesEs);
                    break;
                case MESSAGES:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messages, rxMessages);
                    break;
                case MESSAGES_EN:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messagesEn, rxMessagesEn);
                    break;
                case SETTINGS:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(settings, rxSettings);
                    break;
                case ALL:
                default:
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(events, rxEvents);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(whitelist, rxWhitelist);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(motds, rxMotds);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(settings, rxSettings);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messagesEs, rxMessagesEs);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messagesEn, rxMessagesEn);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(messages, rxMessages);
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
            try (InputStream in = plugin.getResourceAsStream(configName + ".yml")) {
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
            try (InputStream in = plugin.getResourceAsStream(fileToSave.getName() + ".yml")) {
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
    public Configuration getControl(GuardianFiles fileToControl) {
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
        getControl(file).getStringList(path).forEach(text -> coloredList.add(ChatColor.translateAlternateColorCodes('&', text)));
        return coloredList;
    }

    public List<String> getContent(GuardianFiles file, String path, boolean getKeys) {
        List<String> rx = new ArrayList<>();
        Configuration section = getControl(file).getSection(path);
        if(section == null) return rx;
        rx.addAll(section.getKeys());
        return rx;
    }

}

