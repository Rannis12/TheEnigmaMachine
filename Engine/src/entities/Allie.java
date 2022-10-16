package entities;

import java.util.HashSet;
import java.util.Set;

public class Allie {

    private Set<Agent> agents;
    private String username;
    private String allieName;


    public Allie(){

    }

    public Allie(String username, String allieName) {
        this.username = username;
        this.allieName = allieName;

        this.agents = new HashSet<>();
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public String getUsername() {
        return username;
    }

    public String getAllieName() {
        return allieName;
    }
}
