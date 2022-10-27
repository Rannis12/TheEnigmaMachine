package logic.enigma;

import resources.jaxb.generated.CTEBattlefield;

import java.io.Serializable;

public class BattleField implements Serializable {

    final private String battleName;
    final private int amountOfAllies;
    final private String difficulty;
    public BattleField(CTEBattlefield cteBattlefield) {
        battleName = cteBattlefield.getBattleName();
        amountOfAllies = cteBattlefield.getAllies();
        difficulty = cteBattlefield.getLevel();
    }

    public String getBattleName() {
        return battleName;
    }

    public int getAmountOfAllies() {
        return amountOfAllies;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
