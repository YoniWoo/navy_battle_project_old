package fr.lernejo.navy_battle;

public class GameStartRequest {
    public String id;
    public String url;
    public String message;

    public GameStartRequest() {
    }

    public GameStartRequest(String id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }
}
