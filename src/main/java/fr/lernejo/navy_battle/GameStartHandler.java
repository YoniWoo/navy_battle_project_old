package fr.lernejo.navy_battle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class GameStartHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if(!"POST".equals(httpExchange.getRequestMethod())) {
            httpExchange.sendResponseHeaders(404, -1);
            return;
        }

        try (InputStream inputStream = httpExchange.getRequestBody()) {
            GameStartRequest gameStartRequest = objectMapper.readValue(inputStream, GameStartRequest.class);
            String serverId = UUID.randomUUID().toString();
            String serverUrl = "http://" + httpExchange.getLocalAddress().getHostName() + ":" + httpExchange.getLocalAddress().getPort();
            String message = "May the best code win;";

            GameStartResponse response = new GameStartResponse(serverId, message, serverUrl);
            String responseBody = objectMapper.writeValueAsString(response);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(202, responseBody.length());
            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                outputStream.write(responseBody.getBytes());
            }
        } catch (JsonParseException | JsonMappingException e) {
            httpExchange.sendResponseHeaders(400, -1);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, -1);
        }
    }
}
