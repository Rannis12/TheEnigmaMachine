package dtos;

import java.io.Serializable;
import java.util.ArrayList;

public class EngineFullDetailsDTO {

    private int usedAmountOfRotors;
    private int rotorsAmount;
    private int reflectorsAmount;
    private int amountOfDecodedStrings;

    private final String plugBoardString;
    private final String chosenReflector;
    private final String rotorsFirstPositions;
    private final String rotorsCurrentPositions;
    private final ArrayList<Integer> usedRotorsOrganization;
    private final ArrayList<Integer> notchesCurrentPlaces;
    private final ArrayList<Integer> notchesFirstPlaces;


    public EngineFullDetailsDTO(int usedAmountOfRotors, int rotorsAmount, int reflectorsAmount, int amountOfDecodedStrings,
                                String plugBoardString, String chosenReflector, String rotorsFirstPositions, ArrayList<Integer> usedRotorsOrganization,
                                ArrayList<Integer> notchesCurrentPlaces, ArrayList<Integer> notchesFirstPlaces, String rotorsCurrentPositions){

        this.amountOfDecodedStrings = amountOfDecodedStrings;
        this.usedAmountOfRotors = usedAmountOfRotors;
        this.rotorsAmount = rotorsAmount;
        this.reflectorsAmount = reflectorsAmount;
        this.plugBoardString = plugBoardString;
        this.chosenReflector = chosenReflector;
        this.rotorsFirstPositions = rotorsFirstPositions;
        this.usedRotorsOrganization = usedRotorsOrganization;

        this.notchesCurrentPlaces = notchesCurrentPlaces;
        this.notchesFirstPlaces = notchesFirstPlaces;
        this.rotorsCurrentPositions = rotorsCurrentPositions;

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
    public String getPlugBoardString() {
        return plugBoardString;
    }
    public String getChosenReflector() {
        return chosenReflector;
    }
    public String getRotorsFirstPositions() {
        return rotorsFirstPositions;
    }
    public ArrayList<Integer> getUsedRotorsOrganization() {
        return usedRotorsOrganization;
    }
    public int getAmountOfDecodedStrings() {
        return amountOfDecodedStrings;
    }
    public ArrayList<Integer> getNotchesCurrentPlaces() {
        return notchesCurrentPlaces;
    }
    public ArrayList<Integer> getNotchesFirstPlaces() {
        return notchesFirstPlaces;
    }
    public String getRotorsCurrentPositions() {
        return rotorsCurrentPositions;
    }
}
