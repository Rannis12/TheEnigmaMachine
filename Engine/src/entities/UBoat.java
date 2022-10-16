package entities;

import logic.enigma.Engine;

import java.util.HashSet;
import java.util.Set;

public class UBoat {
    private String battleName;
    private String difficulty;
    private int maxAmountOfAllies;
    private Set<Allie> allies;

    private Engine engine;

    public UBoat(){
        allies = new HashSet<>();
//        engine = new Engine();
    }

    public UBoat(String battleName, String difficulty, int maxAmountOfAllies) {
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.maxAmountOfAllies = maxAmountOfAllies;

        this.allies = new HashSet<>();
    }

    public String getBattleName() {
        return battleName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Set<Allie> getAllies() {
        return allies;
    }

    public int getMaxAmountOfAllies() {
        return maxAmountOfAllies;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void addAllie(Allie allie) { //YET CHECKED!!
        allies.add(allie);
    }
}
