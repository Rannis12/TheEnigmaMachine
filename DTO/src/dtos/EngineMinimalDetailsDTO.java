package dtos;

import java.io.Serializable;

public class EngineMinimalDetailsDTO implements Serializable {

    private final int usedAmountOfRotors;
    private final int rotorsAmount;
    private final int reflectorsAmount;

    public EngineMinimalDetailsDTO(int usedAmountOfRotors, int rotorsAmount, int reflectorsAmount) {
        this.usedAmountOfRotors = usedAmountOfRotors;
        this.rotorsAmount = rotorsAmount;
        this.reflectorsAmount = reflectorsAmount;
    }


    public int getUsedAmountOfRotors() {
        return usedAmountOfRotors;
    }
    public int getRotorsAmount() {
        return rotorsAmount;
    }
    public int getReflectorsAmount() {
        return reflectorsAmount;
    }
}
