package fr.lernejo.navy_battle;

public class AttackResponse {
    private final String consequence;
    private final boolean shipLeft;

    public AttackResponse(String consequence, boolean shipLeft) {
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
