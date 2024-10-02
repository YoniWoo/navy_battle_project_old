package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = Integer.parseInt(args[0]);
        HttpServer server = server(port);
        server.start();
        if (args.length == 2) {
            sendPostToEnnemy(args[1], port);
        }
    }

    private static HttpServer server(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        GameBoard gameBoard = new GameBoard();
        server.createContext("/ping", t -> {
            String body = "OK";
            t.sendResponseHeaders(200, body.length());
            t.getResponseBody().write(body.getBytes());
            t.getResponseBody().close();
        });
        server.createContext("/api/game/start", new GameStartHandler());
        server.createContext("/api/game/fire", new FireHandler(gameBoard));
        server.setExecutor(Executors.newFixedThreadPool(1));
        return server;
    }

    private static void sendPostToEnnemy(String url, int port) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        GameStartRequest myInfo = new GameStartRequest(UUID.randomUUID().toString(), "http://localhost:" + port, "hello");
        String requestBody = mapper.writeValueAsString(myInfo);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Réponse de l'adversaire: " + response.statusCode() + " - " + response.body());
    }
}
