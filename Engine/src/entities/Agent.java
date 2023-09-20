package entities;

import java.io.Serializable;

public class Agent  {

    private String agentName;
    private String allieName;
    private int amountOfThreads;
    private int amountOfMissions;
    private int missionsFinished;
    private int amountOfCandidates;
    private String uBoatName;

    public Agent(){
    }



    public Agent(String agentName, String allieName, int amountOfThreads, int amountOfMissions, int amountOfCandidates) {
        this.agentName = agentName;
        this.allieName = allieName;
        this.amountOfThreads = amountOfThreads;
        this.amountOfMissions = amountOfMissions;
        this.amountOfCandidates = amountOfCandidates;
    }

    public int getAmountOfCandidates() {
        return amountOfCandidates;
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

    public void setAmountOfCandidates(int amountOfCandidates) {
        this.amountOfCandidates = amountOfCandidates;
    }

    public String getUBoatName() {
        return uBoatName;
    }

    public void setUBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public void initUBoatName() {
        uBoatName = "";
    }
}
