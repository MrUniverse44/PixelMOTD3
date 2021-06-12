package dev.mruniverse.pixelmotd.spigot.injector;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.mruniverse.pixelmotd.spigot.injector.modules.ConfigurationModule;
import org.bukkit.plugin.Plugin;

public class ModuleController extends AbstractModule {

    private final Plugin plugin;

    public ModuleController(Plugin plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    protected void configure() {

        bind(Plugin.class).toInstance(plugin);

        install(new ConfigurationModule(plugin));

    }
}