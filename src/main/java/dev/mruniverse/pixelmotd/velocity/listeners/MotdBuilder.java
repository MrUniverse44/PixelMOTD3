package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.util.Favicon;
import dev.mruniverse.pixelmotd.global.GLogger;
import dev.mruniverse.pixelmotd.global.enums.IconFolders;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.velocity.PixelMOTDBuilder;

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
    }

    public Favicon getFavicon(MotdType motdType, String key) {
        Map<String, Icon> icons = this.icons.get(motdType);
        if (key.equalsIgnoreCase("RANDOM")) {
            List<Icon> values = new ArrayList<>(icons.values());
            int randomIndex = random.nextInt(values.size());
            return values.get(randomIndex).getFavicon();
        }
        return icons.get(key) == null
                ? null
                : icons.get(key).getFavicon();
    }

}
