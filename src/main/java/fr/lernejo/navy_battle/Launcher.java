package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -jar NAVY_BATTLE.jar <port>");
        }
        int port = Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new GameStartHandler());
        server.setExecutor(Executors.newFixedThreadPool(1));
        server.start();

        System.out.println("Server started on port " + port);
    }

    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if("GET".equals(t.getRequestMethod())) {
                String body = "OK";
                t.sendResponseHeaders(200, body.length());
                try (OutputStream os = t.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }
    }
}
