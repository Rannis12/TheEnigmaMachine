package entities;

import decryption.dm.DecryptionManager;
import dtos.web.DecryptTaskDTO;
import logic.enigma.Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Allie  {

    private Set<Agent> agents;
    private String uBoatUsername;
    private String allieName;
    private int missionSize;
    private boolean status;
    private int amountOfMissionsInQueue;
    private int currentMissionsInQueue;
    private DecryptionManager decryptionManager;
    private BlockingQueue<DecryptTaskDTO> blockingQueue = new LinkedBlockingQueue<>(1000);
    private boolean confirmLogout;
    private boolean contestIsOver;

    public Allie(String allieName){
        this.allieName = allieName;
        this.status = false;
        this.agents = new LinkedHashSet<>();
        confirmLogout = false;
        contestIsOver = false;
    }

    public Allie(String allieName, String uBoatUsername) {
        this.allieName = allieName;
        this.status = false;
        this.agents = new LinkedHashSet<>();
        this.uBoatUsername = uBoatUsername;
        confirmLogout = false;
        contestIsOver = false;
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

    public String getUBoatUsername() {
        return uBoatUsername;
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
        decryptionManager = new DecryptionManager(
                (Engine)engineToClone.clone(), difficulty,
                missionSize, blockingQueue);

        amountOfMissionsInQueue = decryptionManager.getAmountOfMissions();
        currentMissionsInQueue = decryptionManager.getAmountOfMissions();
    }

    public int getInitAmountOfMissionsInQueue() {
        return amountOfMissionsInQueue;
    }

    public void encode() {
        decryptionManager.encode();
    }

    public List<DecryptTaskDTO> pollMissions(int amountToPoll){

        List<DecryptTaskDTO> dtoList = new ArrayList<>();

        synchronized (blockingQueue){
            for (int i = 0; i < amountToPoll; i++) {
                DecryptTaskDTO dto = blockingQueue.poll();
                currentMissionsInQueue--;
                if(dto != null){
                    dtoList.add(dto);
                }else {
                    break;
                }
            }
        }

        return dtoList;
    }

    public int getCurrentMissionsInQueue() {
        return currentMissionsInQueue;
    }

    public int getTotalMissionsCompleted(){
        int totalMissionsCompleted = 0;
        for (Agent agent : agents) {
            totalMissionsCompleted += agent.getMissionsFinished();
        }

        return totalMissionsCompleted;
    }

    public void initUBoatName() {
        uBoatUsername = "";
    }

    public void setUBoatUsername(String uBoatName) {
        this.uBoatUsername = uBoatName;
    }

    public void setConfirmLogout(boolean b) {
        confirmLogout = b;
    }

    public boolean isConfirmLogout() {
        return confirmLogout;
    }

    public void setContestIsOver(boolean b) {
        this.contestIsOver = b;
    }

    public boolean getContestIsOver() {
        return this.contestIsOver;
    }
}
