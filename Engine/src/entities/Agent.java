package entities;

public class Agent {

    private final String agentName;
    private final String allieName;
    private final int amountOfThreads;
    private final int amountOfMissions;


    public Agent(String agentName, String allieName, int amountOfThreads, int amountOfMissions) {
        this.agentName = agentName;
        this.allieName = allieName;
        this.amountOfThreads = amountOfThreads;
        this.amountOfMissions = amountOfMissions;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllieName() {
        return allieName;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public int getAmountOfMissions() {
        return amountOfMissions;
    }
}
