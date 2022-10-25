package entities;

import decryption.dm.DecryptionManager;
import dtos.web.DecryptTaskDTO;
import logic.enigma.Engine;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Allie implements Serializable {

    private Set<Agent> agents;
    private String username; // maybe we won't need it.
    private String allieName;
    private int missionSize;
    private boolean status;

    private DecryptionManager decryptionManager;


    public Allie(){

    }

    public Allie(String allieName) {
        this.allieName = allieName;
        this.status = false;
        this.agents = new LinkedHashSet<>();
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void addAgent(Agent agent){
        agents.add(agent);
    }

    public void setMissionSize(int missionSize){
        this.missionSize = missionSize;
    }


    public int getAmountOfAgents(){
        return agents.size();
    }

    public String getUsername() {
        return username;
    }

    public String getAllieName() {
        return allieName;
    }

    public int getMissionSize(){
        return this.missionSize;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setDecryptionManager(Engine engineToClone, String difficulty){
        decryptionManager = new DecryptionManager((Engine)engineToClone.clone(), difficulty, missionSize);
    }

    public void encode() {
        decryptionManager.encode();
    }

    public DecryptTaskDTO pollOneMission() {
        return decryptionManager.pollOneMission();
    }
}
