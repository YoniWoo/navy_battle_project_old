package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class FireHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GameBoard gameBoard;

    public FireHandler(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!"GET".equals(httpExchange.getRequestMethod())) {
            httpExchange.sendResponseHeaders(404, -1);
            return;
        }
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> params = parseQuery(query);
        String cell = params.get("cell");
        if (cell == null || !isValidCell(cell)) {
            httpExchange.sendResponseHeaders(400, -1);
            return;
        }

        handleFireAtCell(httpExchange, cell);
    }
    private void handleFireAtCell(HttpExchange httpExchange, String cell) throws IOException {
        FireResult result = gameBoard.fireAt(cell.toUpperCase());
        FireResponse response = new FireResponse(result.getConsequence(), result.areShipsLeft());
        sendJsonResponse(httpExchange, response);
    }
    private void sendJsonResponse(HttpExchange httpExchange, FireResponse response) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, jsonResponse.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
    private boolean isValidCell(String cell) {
        return cell.matches("^[A-Ja-j](10|[0-9])$");
    }
    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            }
        }
        return result;
    }
}
