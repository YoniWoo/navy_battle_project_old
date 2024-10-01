package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException("One or two arguments are required!");
        }

        int port = Integer.parseInt(args[0]);
        String ennemyUrl = args.length == 2 ? args[1] : null;

        GameBoard gameBoard = new GameBoard();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new GameStartHandler());
        server.createContext("/api/game/fire", new FireHandler(gameBoard));
        server.setExecutor(Executors.newFixedThreadPool(1));
        server.start();
        System.out.println("Server started on port " + port);

        if (ennemyUrl != null) {
            try {
                sendPostToEnnemy(ennemyUrl, port);
            } catch (InterruptedException e) {
                System.err.println("Error during POST request: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
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
