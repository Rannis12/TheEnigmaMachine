package dtos.entities;

public class UBoatDTO {

    private String battleName;
    private String difficulty;
    private boolean gameStatus;
    private int maxAmountOfAllies;
    private int currentAmountOfAllies;

    public UBoatDTO(String battleName, String difficulty, boolean gameStatus, int maxAmountOfAllies, int currentAmountOfAllies) {
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.gameStatus = gameStatus;
        this.maxAmountOfAllies = maxAmountOfAllies;
        this.currentAmountOfAllies = currentAmountOfAllies;
    }

    public String getBattleName() {
        return battleName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public int getMaxAmountOfAllies() {
        return maxAmountOfAllies;
    }

    public int getCurrentAmountOfAllies() {
        return currentAmountOfAllies;
    }
}
