package dev.floffah.packswitcher.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.packswitcher.PackSwitcherBungeeCord;
import dev.floffah.packswitcher.store.Pack;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Messages implements Listener {
    PackSwitcherBungeeCord main;

    public Messages(PackSwitcherBungeeCord plugin) {
        main = plugin;
    }

    @EventHandler
    public void onRequest(PluginMessageEvent e) {
        if(e.getTag().equalsIgnoreCase("packswitcher")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String sc = in.readUTF();

            boolean should = false;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            if(sc.equalsIgnoreCase("getpacks")) {
                should = true;
                out.writeUTF("gotpacks");
                out.writeInt(main.packs.amount());
                for(Pack pack : main.packs.toList()) {
                    out.writeUTF(pack.name);
                }
            } else if(sc.equalsIgnoreCase("getpack")) {
                should = false;
                String pack = in.readUTF();
                out.writeUTF("gotpack");
                if(main.packs.has(pack)) {
                    Pack pack1 = main.packs.find(pack);
                    out.writeUTF(pack1.name);
                    out.writeUTF(pack1.url);
                    out.writeUTF(pack1.hash);
                }
            }

            if(should) {
                Server receiver = null;
                if(e.getReceiver() instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) e.getReceiver();
                    receiver = player.getServer();
                } else if(e.getReceiver() instanceof Server) {
                    receiver = (Server) e.getReceiver();
                }
                if(receiver != null) {
                    receiver.getInfo().sendData("packswitcher", out.toByteArray());
                } else {
                    System.err.println("Somehow a " + sc + " event was received from a paper server but a receiver was not sent. CONFUSION.");
                }
            }
        }
    }
}
