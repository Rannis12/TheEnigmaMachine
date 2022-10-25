package dtos.engine;

import dtos.entities.UBoatDTO;

import java.io.Serializable;

public class EngineMinimalDetailsDTO implements Serializable {

    private final int usedAmountOfRotors;
    private final int rotorsAmount;
    private final int reflectorsAmount;
    private final int amountOfDecodedStrings;
    private UBoatDTO uBoatDTO;

    private DictionaryDTO dictionaryDTO;

    public EngineMinimalDetailsDTO(int usedAmountOfRotors, int rotorsAmount, int reflectorsAmount, int amountOfDecodedStrings,
                                   DictionaryDTO dictionaryDTO) {
        this.usedAmountOfRotors = usedAmountOfRotors;
        this.rotorsAmount = rotorsAmount;
        this.reflectorsAmount = reflectorsAmount;
        this.amountOfDecodedStrings = amountOfDecodedStrings;
        this.dictionaryDTO = dictionaryDTO;
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

    public UBoatDTO getUBoatDTO() {
        return uBoatDTO;
    }

    public void setUBoatDTO(String battleName, String difficulty) { //bad implementation!!! delete
        this.uBoatDTO = new UBoatDTO(battleName, difficulty, false, 0, 0);
    }

    public DictionaryDTO getDictionaryDTO() {
        return dictionaryDTO;
    }
}
