package dev.floffah.packswitcher;

import dev.floffah.packswitcher.store.PackStore;
import dev.floffah.packswitcher.util.Messages;
import dev.floffah.util.WebManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class PackSwitcherBungeeCord extends Plugin {
    public WebManager manager;

    public PackStore packs;

    public File configf;
    public Configuration config;

    @Override
    public void onEnable() {
        try {
            if (!Files.exists(Paths.get(getDataFolder() + "/config.yml"))) {
                configf = new File(getDataFolder() + "/config.yml");
                InputStream configstream = getResourceAsStream("config.yml");

                Files.copy(configstream, configf.toPath(), StandardCopyOption.REPLACE_EXISTING);

                configstream.close();

                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configf);
            }
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Could not load or create config. Disabling...");
            onDisable();
            return;
        }

        if(config.getBoolean("enableWeb")) {
            manager = new WebManager(config.getInt("webPort"), config.getString("webIp"));
        }

        packs = new PackStore(config.getSection("packs"), manager);

        getProxy().registerChannel("packswitcher");
        getProxy().getPluginManager().registerListener(this, new Messages(this));

        getLogger().info("Loaded PackSwitcher for Bungeecord");
    }

    @Override
    public void onDisable() {

    }
}
