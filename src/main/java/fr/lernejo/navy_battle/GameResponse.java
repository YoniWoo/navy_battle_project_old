package fr.lernejo.navy_battle;

public class GameResponse {
    private final String id;
    private final String url;
    private final String message;

    public GameResponse(String id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }

    public String getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public String getMessage() {
        return message;
    }
}
