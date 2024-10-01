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
            handleGameStart(httpExchange, inputStream);
        } catch (JsonParseException | JsonMappingException e) {
            httpExchange.sendResponseHeaders(400, -1);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, -1);
        }
    }

    private void handleGameStart(HttpExchange httpExchange, InputStream requestBody) throws IOException {
        GameStartRequest gameStartRequest = objectMapper.readValue(requestBody, GameStartRequest.class);
        System.out.println(gameStartRequest);
        GameStartResponse response = createResponse(httpExchange);
        sendResponse(httpExchange, response);
    }

    private GameStartResponse createResponse(HttpExchange httpExchange) {
        return new GameStartResponse(
            UUID.randomUUID().toString(),
            "http://" + httpExchange.getLocalAddress().getHostName() + ":" + httpExchange.getLocalAddress().getPort(),
            "May the best code win"
        );
    }

    private void sendResponse(HttpExchange httpExchange, GameStartResponse response) throws IOException {
        String responseBody = objectMapper.writeValueAsString(response);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(202, responseBody.length());
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(responseBody.getBytes());
        }
    }
}
