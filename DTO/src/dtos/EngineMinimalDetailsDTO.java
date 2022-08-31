package dtos;

import java.io.Serializable;

public class EngineMinimalDetailsDTO implements Serializable {

    private final int usedAmountOfRotors;
    private final int rotorsAmount;
    private final int reflectorsAmount;

    private final int amountOfDecodedStrings;
    public EngineMinimalDetailsDTO(int usedAmountOfRotors, int rotorsAmount, int reflectorsAmount, int amountOfDecodedStrings) {
        this.usedAmountOfRotors = usedAmountOfRotors;
        this.rotorsAmount = rotorsAmount;
        this.reflectorsAmount = reflectorsAmount;
        this.amountOfDecodedStrings = amountOfDecodedStrings;
    }

    public int getAmountOfDecodedStrings() {
        return amountOfDecodedStrings;
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
