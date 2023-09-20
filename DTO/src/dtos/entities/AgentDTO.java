package dtos.entities;

public class AgentDTO {
    private final String agentName;
    private final String allie;
    private final int amountOfThreads;
    private final int amountOfMissions;
    private int missionsFinished;

    private int amountOfCandidates; //important in order to update the table view in contest page.

    public AgentDTO(String agentName, String allie, int amountOfThreads, int amountOfMissions, int missionsFinished,
                    int amountOfCandidates) {
        this.agentName = agentName;
        this.allie = allie;
        this.amountOfThreads = amountOfThreads;
        this.amountOfMissions = amountOfMissions;
        this.missionsFinished = missionsFinished;
        this.amountOfCandidates = amountOfCandidates;
    }

    public int getAmountOfCandidates() {
        return amountOfCandidates;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllie() {
        return allie;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public int getAmountOfMissions() {
        return amountOfMissions;
    }

    public int getMissionsFinished() {
        return missionsFinished;
    }

    /*public void setMissionsFinished(int missionsFinished) {
        this.missionsFinished = missionsFinished;
    }*/
}
