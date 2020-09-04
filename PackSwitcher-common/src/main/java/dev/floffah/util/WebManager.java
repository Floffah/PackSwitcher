package dev.floffah.util;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class WebManager {
    public int port;
    public String ip;

    HttpServer server;

    public WebManager(int webport, String webip) {
        port = webport;
        ip = webip;
    }

    public boolean start() {
        try {
            server = Vertx.vertx().createHttpServer();
            server.requestHandler(this::handler);
            server.listen(port);
        } catch (Exception e) {
            System.err.println("Could not bind to port " + port + "for PackSwitcher resource packs.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void stop() {
        server.close();
    }

    public void handler(HttpServerRequest req) {

    }
}
