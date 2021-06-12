package dev.mruniverse.pixelmotd.spigot.injector.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import dev.mruniverse.pixelmotd.spigot.storage.Configuration;
import org.bukkit.plugin.Plugin;

public class ConfigurationModule extends AbstractModule {

    private final Plugin plugin;

    public ConfigurationModule(Plugin plugin){
        this.plugin = plugin;
    }

    protected void configure() {

        bind(Configuration.class).annotatedWith(Names.named("motds")).toProvider(() -> new Configuration(plugin, "motds"));
        bind(Configuration.class).annotatedWith(Names.named("events")).toProvider(() -> new Configuration(plugin, "events"));
        bind(Configuration.class).annotatedWith(Names.named("whitelist")).toProvider(() -> new Configuration(plugin, "whitelist"));

    }
}
