package dev.mruniverse.pixelmotd.spigot.storage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final Plugin plugin;
    private final File folder;

    public Configuration(Plugin plugin, String fileName, String fileExtension,
                         File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public Configuration(Plugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public Configuration(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    public String getString(@NotNull String path) {
        String currentPath = super.getString(path);
        if(currentPath == null) currentPath = "invalid path";
        return ChatColor.translateAlternateColorCodes('&', currentPath);
    }

    public String getStringWithoutColors(String path) {
        return super.getString(path);
    }

    public List<String> getColoredList(String path) {
        List<String> coloredList = new ArrayList<>();
        super.getStringList(path).forEach(text -> {
            coloredList.add(ChatColor.translateAlternateColorCodes('&', text));
        });
        return coloredList;
    }

    private void createFile() {

        System.out.println(fileName);

        try {
            File file = new File(folder, fileName);

            if (file.exists()) {
                load(file);
                save(file);
                return;
            }

            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            } else {
                save(file);
            }

            load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Creation of Configuration '" + fileName + "' failed.", e);
        }
    }

    public void save() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);
        try {
            save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Save of the file '" + fileName + "' failed.", e);
        }
    }

    public void reload() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + fileName + "' failed.", e);
        }
    }
}
