package dev.mruniverse.pixelmotd.bungeecord.listeners;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.GLogger;
import dev.mruniverse.pixelmotd.global.enums.IconFolders;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import net.md_5.bungee.api.Favicon;

import java.io.File;
import java.util.*;

public class MotdBuilder {
    private final Map<MotdType, Map<String, Icon>> icons = new HashMap<>();

    private final Random random = new Random();

    private final PixelMOTDBuilder plugin;

    private final GLogger logs;

    public MotdBuilder(PixelMOTDBuilder plugin, GLogger logs) {
        this.plugin = plugin;
        this.logs = logs;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        icons.clear();

        load(MotdType.NORMAL);
        load(MotdType.WHITELIST);
        load(MotdType.BLACKLIST);
        load(MotdType.OUTDATED_SERVER);
        load(MotdType.OUTDATED_CLIENT);

    }

    private void load(MotdType motdType) {

        final Map<String, Icon> iconsPerType = icons.computeIfAbsent(
                motdType,
                l -> new HashMap<>()
        );

        File folder = IconFolders.fromText(plugin.getStorage().getFiles(), motdType);

        File[] files = folder.listFiles((d, fn) -> fn.endsWith(".png"));

        if (files == null) {
            icons.put(motdType, iconsPerType);
            return;
        }

        for (File icon : files) {
            iconsPerType.put(
                    icon.getName(),
                    new Icon(
                            logs,
                            motdType,
                            icon
                    )
            );
        }
        icons.put(motdType, iconsPerType);
    }

    public Favicon getFavicon(MotdType motdType, String key) {
        Map<String, Icon> icons = this.icons.get(motdType);

        if (icons == null) {
            load(motdType);
            icons = this.icons.get(motdType);
        }

        if (key.equalsIgnoreCase("RANDOM")) {
            List<Icon> values = new ArrayList<>(icons.values());
            int randomIndex = random.nextInt(values.size());
            return values.get(randomIndex).getFavicon();
        }
        if (icons.containsKey(key)) {
            return icons.get(key).getFavicon();
        }
        return null;
    }
}
