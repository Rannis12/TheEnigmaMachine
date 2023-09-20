package entities;

import logic.enigma.Engine;
import resources.jaxb.generated.CTEBattlefield;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class UBoat  {
    private String battleName;
    private String difficulty;
    private boolean gameStatus;
    private int maxAmountOfAllies;
    private int currentAmountOfAllies;
    private String inputStreamAsString;
    private Set<Allie> allies;
    private boolean thereIsAWinner;
    private String toDecode;
    private Engine engine;
    private String toEncode;

    public UBoat(){
        allies = new LinkedHashSet<>();
    }
    public UBoat(String battleName, String difficulty, int maxAmountOfAllies) {
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.maxAmountOfAllies = maxAmountOfAllies;
        this.gameStatus = false;
        this.currentAmountOfAllies = 0;

        this.thereIsAWinner = false;
        this.allies = new HashSet<>();
    }
    public UBoat(CTEBattlefield cteBattlefield) {
        difficulty = cteBattlefield.getLevel();
        maxAmountOfAllies = cteBattlefield.getAllies();
        battleName = cteBattlefield.getBattleName();
        gameStatus = false;

        allies = new HashSet<>();
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
    public void addAllie(Allie allie) {
        allies.add(allie);
        currentAmountOfAllies++;
    }
    public boolean getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(boolean gameStatus){
        this.gameStatus = gameStatus;
    }
    public int getCurrentAmountOfAllies() {
        return currentAmountOfAllies;
    }
    public boolean isExistInAllieSet(Allie allie) {
        return allies.contains(allie);
    }
    public Set<String> getAlliesNames(){
        Set<String> alliesNames = new HashSet<>();
        for (Allie allie : allies) {
            alliesNames.add(allie.getAllieName());
        }
        return alliesNames;
    }
    public void setAfterCtor(String battleName, String difficulty, int maxAmountOfAllies){
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.maxAmountOfAllies = maxAmountOfAllies;
        this.gameStatus = false;
    }
    public boolean isThereIsAWinner() {
        return thereIsAWinner;
    }
    public void setThereIsAWinner(boolean value) {
        this.thereIsAWinner = value;
    }
    public String getToDecode() {
        return toDecode;
    }
    public void setToDecode(String toDecode) {
        this.toDecode = toDecode;
    }
    public void setToEncode(String toEncode){
        this.toEncode = toEncode;
    }
    public String getToEncode() {
        return toEncode;
    }

}
