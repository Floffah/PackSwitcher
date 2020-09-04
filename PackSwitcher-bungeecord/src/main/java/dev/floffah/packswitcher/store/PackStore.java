package dev.floffah.packswitcher.store;

import dev.floffah.util.WebManager;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackStore {
    Configuration config;
    WebManager manager;

    HashMap<String, Pack> packs;

    public PackStore(Configuration packconf, WebManager webManager) {
        config = packconf;
        packs = new HashMap<String, Pack>();
        manager = webManager;

        for(String pack : config.getStringList("enablePacks")) {
            Pack pack1 = find(pack);
            if(pack1 == null) {
                System.err.println("Enabled pack " + pack + " does not exist in section packs of PackSwitcher config.");
            }
        }
    }

    public List<Pack> toList() {
        List<Pack> list = new ArrayList<Pack>();
        for(String pack : packs.keySet()) {
            list.add(packs.get(pack));
        }
        return list;
    }

    public int amount() {
        return packs.size();
    }

    public boolean has(String name) {
        return packs.containsKey(name) || config.getSection(name) != null;
    }

    public Pack find(String name) {
        if(packs.containsKey(name)) {
            return packs.get(name);
        } else {
            if(config.getSection(name) != null) {
                Pack found = new Pack(config.getSection(name), manager.ip, manager.port, name);
                packs.put(name, found);
                return found;
            } else {
                return null;
            }
        }
    }
}