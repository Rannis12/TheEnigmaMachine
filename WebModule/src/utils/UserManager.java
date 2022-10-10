package utils;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersSet; //UBoats
    private final Set<String> allies;
    private final Set<String> agents;

    public UserManager() {
        usersSet = new HashSet<>();
        allies = new HashSet<>();
        agents = new HashSet<>();

    }

    public synchronized void addUser(String name, String type) {
        switch(type){
            case "UBoat":
                usersSet.add(name);
                break;
            case "Allie":
                allies.add(name);
                break;
            case "Agent":
                agents.add(name);
                break;
        }
//        usersSet.add(username);

        /*//checking
        for (String str:usersSet) {
            System.out.println(str);
        }
        System.out.println("done.");*/
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return (usersSet.contains(username) || allies.contains(username) || agents.contains(username));
    }
}
