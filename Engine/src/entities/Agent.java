package entities;

import java.io.Serializable;

public class Agent  {

    private String agentName;
    private String allieName;
    private int amountOfThreads;
    private int amountOfMissions;
    private int missionsFinished;

    public Agent(){
    }

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

    public void setMissionsCompletedSoFar(int missionsFinished) {
        this.missionsFinished = missionsFinished;
    }

    public int getMissionsFinished() {
        return missionsFinished;
    }
}
