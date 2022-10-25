package dtos.web;

import java.util.HashSet;
import java.util.Set;

public class ContestDetailsDTO {
    final private Set<String> allies;
    final private String battleFieldName;
    final private String username;
    final private boolean gameStatus;
    final private String difficulty;
    final private int currentAmountOfTeams;
    final private int maxAmountOfTeams;

    public ContestDetailsDTO(Set<String> allies, String battleFieldName, String username, boolean gameStatus, String difficulty,
                             int currentAmountOfTeams, int maxAmountOfTeams) {

        this.allies = allies;
        this.battleFieldName = battleFieldName;
        this.username = username;
        this.gameStatus = gameStatus;
        this.difficulty = difficulty;

        this.currentAmountOfTeams = currentAmountOfTeams;
        this.maxAmountOfTeams = maxAmountOfTeams;

    }

    public Set<String> getAlliesNames() {
        return allies;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getUsername() {
        return username;
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTeams() {
        return String.valueOf(currentAmountOfTeams) + '/' + maxAmountOfTeams;
    }

    public int getCurrentAmountOfTeams() {
        return currentAmountOfTeams;
    }

    public int getMaxAmountOfTeams() {
        return maxAmountOfTeams;
    }
}
