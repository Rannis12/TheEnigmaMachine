package utils;

import dtos.web.ContestDetailsDTO;
import entities.Agent;
import entities.Allie;
import entities.UBoat;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersSet; //UBoats
    private final Map<String, UBoat> usernameToUBoatMap;
    private final Map<String, Allie> alliesMap;
    private final Map<String, Agent> agentsMap;
    private final Map<String, String> battleFieldNameToUserNameMap;

    public UserManager() {
        usersSet = new HashSet<>();
        usernameToUBoatMap = new HashMap<>();
        alliesMap = new HashMap<>();
        agentsMap = new HashMap<>();
        battleFieldNameToUserNameMap = new HashMap<>();
//        allies = new HashSet<>();
//        agents = new HashSet<>();

    }

    //parentName should be someone is above you in the tree. for example, Allie's Parent is UBoat, and UBoat doesn't have a parent.
    public synchronized void addUser(String name, String type, String parentName) {
        usersSet.add(name);
        switch(type){
            case "UBoat":
                usernameToUBoatMap.put(name, new UBoat());
                break;
            case "Allie":
                alliesMap.put(name, new Allie());

                //YET CHECKED!!
//                usernameToUBoatMap.get(parentName)
//                        .addAllie(alliesMap.get(name));

                break;
            case "Agent":
//                agents.add(name);
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
//        return (usersSet.contains(username) || allies.contains(username) || agents.contains(username));
        return usersSet.contains(username);
    }

    public UBoat getUBoat(String name){
        return usernameToUBoatMap.get(name);
    }

    public void addUBoat(String name){
        usernameToUBoatMap.put(name, new UBoat());
    }

    public synchronized Set<String> getBattleFieldNames() {
        Set<String> battleFieldNames = new HashSet<>();

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            battleFieldNames.add(entry.getValue().getBattleName());
        }
            return battleFieldNames;
    }

    public synchronized Set<ContestDetailsDTO> getBattleFields() {
        Set<ContestDetailsDTO> battleFieldNames = new HashSet<>();

        for (Map.Entry<String,UBoat> entry : usernameToUBoatMap.entrySet()) {
            UBoat uBoat= entry.getValue();
            String username = entry.getKey();

            battleFieldNames.add(new ContestDetailsDTO(uBoat.getBattleName(), username, uBoat.getGameStatus(),
                    uBoat.getDifficulty(), "need to create the string."));
        }
        return battleFieldNames;
    }

    public void setUBoat(UBoat uBoat, String username) {
        usernameToUBoatMap.put(username, uBoat);

    }
}
