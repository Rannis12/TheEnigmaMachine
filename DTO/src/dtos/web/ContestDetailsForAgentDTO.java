package dtos.web;

public class ContestDetailsForAgentDTO {

    private final String allyName;
    private final String battleName;
    private final String difficulty;
    private final boolean contestStatus;
    private final int amountOfTeams;


    public ContestDetailsForAgentDTO(String allyName, String battleName, String difficulty, boolean contestStatus, int amountOfTeams) {
        this.allyName = allyName;
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.contestStatus = contestStatus;
        this.amountOfTeams = amountOfTeams;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getBattleName() {
        return battleName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getAmountOfTeams() {
        return amountOfTeams;
    }

    public String getContestStatus(){
        if(contestStatus){
            return "Ready";
        }
        return "Not Ready";
    }
}
