package dtos.web;

public class ContestDetailsDTO {
    private String battleFieldName;
    private String username;
    private String gameStatus;
    private String difficulty;
    private String teams;

    public ContestDetailsDTO(String battleFieldName, String username, String gameStatus, String difficulty, String teams) {
        this.battleFieldName = battleFieldName;
        this.username = username;
        this.gameStatus = gameStatus;
        this.difficulty = difficulty;
        this.teams = teams;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getUsername() {
        return username;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTeams() {
        return teams;
    }
}
