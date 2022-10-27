package utils;

import dtos.MissionDTO;
import dtos.TeamInformationDTO;
import dtos.entities.AgentDTO;
import dtos.entities.AllieDTO;
import dtos.web.ContestDetailsDTO;
import dtos.web.DecryptTaskDTO;
import dtos.web.ShouldStartContestDTO;
import entities.Agent;
import entities.Allie;
import entities.UBoat;
import logic.enigma.BattleField;
import logic.enigma.Engine;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization, and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersSet; //UBoats
    private final Map<String, UBoat> usernameToUBoatMap;
    private final Map<String, Allie> alliesMap;
    private final Map<String, Agent> agentsMap;
    private final Map<String, String> uBoatsNameToInputStreamString;
    private final Map<String, List<MissionDTO>> uBoatNameToCandidates;

    public UserManager() {
        usersSet = new HashSet<>();
        usernameToUBoatMap = new LinkedHashMap<>();
        alliesMap = new LinkedHashMap<>();
        agentsMap = new LinkedHashMap<>();
        uBoatsNameToInputStreamString = new LinkedHashMap<>();
        uBoatNameToCandidates = new LinkedHashMap<>();

    }

    //parentName should be someone is above you in the tree. for example, Allie's Parent is UBoat, and UBoat doesn't have a parent.
    public synchronized void addUser(String name, String type) {
        usersSet.add(name);
        switch(type){
            case "UBoat":
                usernameToUBoatMap.put(name, new UBoat());
                uBoatNameToCandidates.put(name, new ArrayList<>()); //? maybe different ctor
                break;
            case "Allie":
                alliesMap.put(name, new Allie());

                break;
            case "Agent":
                agentsMap.put(name, new Agent());
                break;
        }

    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public synchronized UBoat getUBoat(String name){
        return usernameToUBoatMap.get(name);
    }

    public synchronized void addUBoat(String name){
        usernameToUBoatMap.put(name, new UBoat());
    }

    public synchronized Set<String> getBattleFieldNames() {
        Set<String> battleFieldNames = new LinkedHashSet<>();

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            battleFieldNames.add(entry.getValue().getBattleName());
        }
            return battleFieldNames;
    }

    public synchronized Set<ContestDetailsDTO> getBattleFields() {
        Set<ContestDetailsDTO> battleFieldNames = new LinkedHashSet<>();

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat= entry.getValue();
            String username = entry.getKey();

            battleFieldNames.add(new ContestDetailsDTO(uBoat.getAlliesNames(), uBoat.getBattleName(), username, uBoat.getGameStatus(),
                    uBoat.getDifficulty(), uBoat.getCurrentAmountOfAllies(), uBoat.getMaxAmountOfAllies()));
        }
        return battleFieldNames;
    }

    public synchronized void setUBoat(BattleField battleField, String username, Engine engine) {
        UBoat uBoat = new UBoat(battleField.getBattleName(), battleField.getDifficulty(), battleField.getAmountOfAllies());
        usernameToUBoatMap.put(username, uBoat);
        uBoat.setEngine(engine);

    }

    public synchronized void setAllie(String usernameFromParameter, String parentName, AllieDTO allie) {

        alliesMap.put(usernameFromParameter, new Allie(allie.getAllieName()));

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(parentName)){
                if(!entry.getValue().isExistInAllieSet(alliesMap.get(usernameFromParameter))) {
                    entry.getValue().addAllie(alliesMap.get(usernameFromParameter));
                    break;
                }
            }
        }
    }

    public synchronized UBoat getUBoatByName(String uBoatName) {
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(uBoatName)){
                return entry.getValue();
            }
        }
        return null;
    }

    public synchronized ContestDetailsDTO getBattleField(String battleName) {
        ContestDetailsDTO battleField = null;

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(battleName)){
                battleField = new ContestDetailsDTO(entry.getValue().getAlliesNames(), entry.getValue().getBattleName(), entry.getKey(), entry.getValue().getGameStatus(),
                        entry.getValue().getDifficulty(), entry.getValue().getCurrentAmountOfAllies(), entry.getValue().getMaxAmountOfAllies());

                break;
            }
        }

        return battleField;
    }

    public synchronized void setAgent(String username, AgentDTO agentDTO) {
        agentsMap.put(username, new Agent(agentDTO.getAgentName(), agentDTO.getAllie(),
                agentDTO.getAmountOfThreads(), agentDTO.getAmountOfMissions()));

        for (Map.Entry<String,Allie> entry : alliesMap.entrySet()) {
            Allie allie = entry.getValue();

            if(allie.getAllieName().equals(agentDTO.getAllie())){
                allie.addAgent(agentsMap.get(username));
                break;
            }
        }
    }

    public synchronized Set<AgentDTO> getAgents() {
        Set<AgentDTO> agentDTOS = new LinkedHashSet<>();

        for (Map.Entry<String,Agent> entry : agentsMap.entrySet()) {
            Agent agent = entry.getValue();
            agentDTOS.add(new AgentDTO(agent.getAgentName(), agent.getAllieName(),
                    agent.getAmountOfThreads(), agent.getAmountOfMissions()));
        }
        return agentDTOS;
    }

    public synchronized Set<TeamInformationDTO> getAlliesBelongsToBattleField(String battleName) {
        Set<TeamInformationDTO> dtoSet = new LinkedHashSet<>();
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat = entry.getValue();

            if(uBoat.getBattleName().equals(battleName)){
                for (Allie allie : uBoat.getAllies()) {
                    dtoSet.add(new TeamInformationDTO(allie.getAllieName(), allie.getAmountOfAgents(), allie.getMissionSize()));
                }
                break;
            }
        }

        return dtoSet;
    }

    public void setAllieStatus(String allieName, String battleField, String isReady, int amountOfMissions) { //need to check it.

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat = entry.getValue();

            if(uBoat.getBattleName().equals(battleField)){
                Set<Allie> allies = uBoat.getAllies();

                for (Allie ally : allies) {
                    if(ally.getAllieName().equals(allieName)){
                        ally.setStatus(fromStringToBoolean(isReady));
                        ally.setMissionSize(amountOfMissions);
                        break;
                    }
                }
            }
        }
    }

    private boolean fromStringToBoolean(String isReady) {
        if(isReady.equals("Ready")){
            return true;
        }
        return false;
    }

    public synchronized void setUBoatStatus(String username, String status) {
        usernameToUBoatMap.get(username).setGameStatus(fromStringToBoolean(status));
    }

    public synchronized ShouldStartContestDTO doesEveryOneReady(String battleField) {
        ShouldStartContestDTO dto;

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat = entry.getValue();

            if(uBoat.getBattleName().equals(battleField)
                    && uBoat.getCurrentAmountOfAllies() == uBoat.getMaxAmountOfAllies()){

                if(uBoat.getGameStatus()){
                    Set<Allie> allies = uBoat.getAllies();
                    for (Allie ally : allies) {
                        if(!ally.getStatus()){
                            dto = new ShouldStartContestDTO("", false);
                            return dto;
                        }
                    }

                    dto = new ShouldStartContestDTO(/*ServletUtils.getToEncodeString()*/ entry.getValue().getToEncode(), true);
                    return dto;
                }
            }
        }

        return new ShouldStartContestDTO("", false);
    }



    public synchronized void startCreateMissions(String allyUserName) {
        UBoat uBoat = getUBoatByGivenAllieName(allyUserName);

        alliesMap.get(allyUserName).setDecryptionManager(uBoat.getEngine(), uBoat.getDifficulty());
        alliesMap.get(allyUserName).encode();

    }

    public synchronized Allie getAlly(String allieName) {
        return alliesMap.get(allieName);
    }

    public synchronized UBoat getUBoatByGivenAllieName(String allyName){
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            for (Allie ally : entry.getValue().getAllies()) {
                if(ally.getAllieName().equals(allyName)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public synchronized void addInputStreamToMap(String userName, String inputStreamAsString) {
        uBoatsNameToInputStreamString.put(userName, inputStreamAsString);
    }

    public synchronized String getInputStreamFromMap(String battleName) {

        String inputStreamAsString = null;
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat = entry.getValue();

            if(uBoat.getBattleName().equals(battleName)){
                inputStreamAsString = uBoatsNameToInputStreamString.get(entry.getKey());
                break;
            }
        }
        return inputStreamAsString;
    }

    public synchronized UBoat getUBoatByGivenAgentName(String agentName) {
        for (Map.Entry<String,Agent> entry : agentsMap.entrySet()) {
            if(entry.getValue().getAgentName().equals(agentName)){
                String allyName = entry.getValue().getAllieName();

                for (Map.Entry<String,Allie> ally : alliesMap.entrySet()) {
                    if(ally.getValue().getAllieName().equals(allyName)){
                        return getUBoatByGivenAllyName(ally.getValue().getAllieName());
                    }
                }

            }
        }
        return null;
    }

    public synchronized UBoat getUBoatByGivenAllyName(String allyName){
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            Set<Allie> allies = entry.getValue().getAllies();

            for (Allie allie : allies){
                if(allie.getAllieName().equals(allyName)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public synchronized List<DecryptTaskDTO> pollMissions(String agentName, int amountOfMissions) {

//        List<DecryptTaskDTO> dtoList = new ArrayList<>();
        List<DecryptTaskDTO> dtoList;
        Allie ally = getAllyNameByGivenAgentName(agentName);

        dtoList = ally.pollMissions(amountOfMissions);

        /*
        for (int i = 0; i < amountOfMissions; i++) {
            DecryptTaskDTO dto = ally.pollOneMission();
            if(dto == null){
                return dtoList;

            }else {
                dtoList.add(dto);
            }
        }*/

        return dtoList;
    }

    public Allie getAllyNameByGivenAgentName(String agentName){
        for (Map.Entry<String,Allie> entry : alliesMap.entrySet()) {
            for (Agent agent : entry.getValue().getAgents()) {
                if(agent.getAgentName().equals(agentName)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public synchronized void addNewCandidates(String battleName, List<MissionDTO> candidatesAsList) {

        String uBoatUsername = null;
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(battleName)){
                uBoatUsername = entry.getKey();
                break;
            }
        }

        List<MissionDTO> tmp = uBoatNameToCandidates.get(uBoatUsername);
        for (MissionDTO dto : candidatesAsList) {
            tmp.add(dto);
        }
    }

    public synchronized List<MissionDTO> getCandidates(String userName, String client) {
        List<MissionDTO> dtoList = null;
        String uBoatName = null;

        if(client.equals("UBoat")) {
            return uBoatNameToCandidates.get(userName);

        }else{ //client is ally

            for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
                Set<Allie> allies = entry.getValue().getAllies();
                for (Allie ally : allies) {
                    if(ally.getAllieName().equals(userName)){
                        uBoatName = entry.getKey();
                        break;
                    }
                }
            }

            List<MissionDTO> tmp = uBoatNameToCandidates.get(uBoatName);
            Set<Agent> agents = getAlly(userName).getAgents();
            dtoList = new ArrayList<>();

            for (MissionDTO dto : tmp) {
                for (Agent agent : agents) {
                    if(agent.getAgentName().equals(dto.getAgentID())){
                        dtoList.add(dto);
                        break;
                    }
                }
            }
        }

        return dtoList;
    }

    public synchronized UBoat getUBoatByGivenBattleName(String battleName) {
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(battleName)){
                return entry.getValue();
            }
        }
        return null;
    }

    public void lookForAWinner(String battleName) {
        String uBoatUsername = null;
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(battleName)){
                uBoatUsername = entry.getKey();
                break;
            }
        }

        List<MissionDTO> tmp = uBoatNameToCandidates.get(uBoatUsername);
        UBoat uBoat = usernameToUBoatMap.get(uBoatUsername);

        for (MissionDTO dto : tmp) {
            if(uBoat.getToDecode().equals(dto.getDecodedTo())){ //todo
                uBoat.setThereIsAWinner(true);
                dto.setWinner(true);
                break;
            }
        }
    }

    public MissionDTO thereIsAWinner(String battleName) {
        String uBoatUsername = null;
        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            if(entry.getValue().getBattleName().equals(battleName)){
                uBoatUsername = entry.getKey();
                break;
            }
        }

        if(usernameToUBoatMap.get(uBoatUsername).isThereIsAWinner()){
            List<MissionDTO> tmp = uBoatNameToCandidates.get(uBoatUsername);
            for (MissionDTO dto : tmp) {
                if(dto.getDecodedTo().equals(usernameToUBoatMap.get(uBoatUsername).getToDecode())){
                    dto.setWinner(true);
                    return dto;
                }
            }
        }

        return null;
    }
}
