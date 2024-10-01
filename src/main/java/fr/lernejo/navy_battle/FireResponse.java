package fr.lernejo.navy_battle;

public class FireResponse {
    private final String consequence;
    private final boolean shipLeft;

    public FireResponse(String consequence, boolean shipLeft) {
        this.consequence = consequence;
        this.shipLeft = shipLeft;
    }

    public String getConsequence() {
        return consequence;
    }

    public boolean isShipLeft() {
        return shipLeft;
    }
}
