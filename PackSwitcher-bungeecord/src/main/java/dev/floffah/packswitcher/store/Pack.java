package dev.floffah.packswitcher.store;

import net.md_5.bungee.config.Configuration;

public class Pack {
    public Configuration pack;

    public String name;
    public String url;
    public String hash;

    public Pack(Configuration packsection, String ip, int port, String packname) {
        pack = packsection;

        url = "http://" + ip + ":" + port + "/" + pack.getString("name");
        hash = pack.getString("hash");
        name = packname;
    }
}
