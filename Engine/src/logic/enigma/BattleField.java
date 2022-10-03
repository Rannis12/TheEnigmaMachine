package logic.enigma;

public class BattleField {
    private String battleName;
    private String difficulty;
    private int allies;

    public BattleField(String battleName, String difficulty, int allies) {
        this.battleName = battleName;
        this.difficulty = difficulty;
        this.allies = allies;
    }

    public String getBattleName() {
        return battleName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getAllies() {
        return allies;
    }
}
