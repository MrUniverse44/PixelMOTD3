package dev.mruniverse.pixelmotd.global.shared;

import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.GLogger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BungeeControl implements Control {

    private final Random random = new Random();

    private final InputStream resource;

    private final GLogger logs;

    private final File file;

    private Configuration configuration;



    public BungeeControl(GLogger logs,File file,InputStream resource) {
        this.file = file;
        this.logs = logs;
        this.resource = resource;
        load();
    }

    @Override
    public Random getRandom() { return random; }

    public BungeeControl(GLogger logs,File file) {
        this.file = file;
        this.logs = logs;
        this.resource = null;
        load();
    }

    @Override
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }catch (Throwable throwable) {
            logs.error("Can't save file: " + file.getName());
            logs.error(throwable);
        }
    }

    @Override
    public List<String> getColoredStringList(String path) {
        List<String> coloredList = new ArrayList<>();
        configuration.getStringList(path).forEach(text -> coloredList.add(ChatColor.translateAlternateColorCodes('&', text)));
        return coloredList;
    }

    @Override
    public String getColoredString(String path) {
        return ChatColor.translateAlternateColorCodes('&',configuration.getString(path,"&cInvalid path: &e" + path));
    }

    @Override
    public String getColoredString(String path,String def) {
        return ChatColor.translateAlternateColorCodes('&',configuration.getString(path,def));
    }

    @Override
    public String getStringWithoutColors(String path) {
        return ChatColor.stripColor(configuration.getString(path,"Invalid path: " + path));
    }

    @Override
    public String getStringWithoutColors(String path,String def) {
        return ChatColor.stripColor(configuration.getString(path,def));
    }

    @Override
    public void reload() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (Throwable throwable) {
            logs.error("Can't reload file: " + file.getName());
            logs.error(throwable);
        }
    }

    public void load() {
        configuration = loadConfig(file);
    }

    public void saveConfig(File fileToSave) {
        if (!fileToSave.getParentFile().exists()) {
            boolean createFile = fileToSave.mkdir();
            if(createFile) logs.info("&7Folder created!!");
        }

        if (!fileToSave.exists()) {
            try (InputStream in = resource) {
                if(in != null) {
                    Files.copy(in, fileToSave.toPath());
                }
            } catch (Throwable throwable) {
                logs.error(String.format("A error occurred while copying the config %s to the plugin data folder. Error: %s", fileToSave.getName(), throwable));
                logs.error(throwable);
            }
        }
    }

    private Configuration loadConfig(File file) {
        if (!file.exists()) {
            saveConfig(file);
        }

        Configuration cnf = null;
        try {
            cnf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (Exception e) {
            logs.warn(String.format("A error occurred while loading the settings file. Error: %s", e));
            e.printStackTrace();
        }

        logs.info(String.format("&7File &e%s &7has been loaded", file.getName()));
        return cnf;
    }

    @Override
    public List<?> getList(String path) {
        return configuration.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return configuration.getList(path,def);
    }

    @Override
    public List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public List<String> getContent(String path, boolean getKeys) {
        List<String> rx = new ArrayList<>();
        Configuration section = configuration.getSection(path);
        if(section == null) return rx;
        rx.addAll(section.getKeys());
        return rx;
    }

    @Override
    public List<Integer> getIntList(String path) {
        return configuration.getIntList(path);
    }

    @Override
    public int getInt(String path, int def) {
        return configuration.getInt(path,def);
    }

    @Override
    public int getInt(String path) {
        return configuration.getInt(path);
    }

    @Override
    public boolean contains(String path) {
        return configuration.contains(path);
    }

    @Override
    public boolean getStatus(String path) {
        return configuration.getBoolean(path);
    }

    @Override
    public boolean getStatus(String path, boolean def) {
        return configuration.getBoolean(path,def);
    }

    @Override
    public void set(String path, Object value) {
        configuration.set(path,value);
    }

    @Override
    public String getString(String path, String def) {
        return configuration.getString(path,def);
    }

    @Override
    public String getString(String path) {
        return configuration.getString(path,"&cInvalid Path: &e" + path);
    }
}
