package dtos.web;

import java.util.ArrayList;

public class DecryptTaskDTO {

    private int sizeOfMission;
    private ArrayList<String> initialPositions;

    public DecryptTaskDTO(int sizeOfMission, ArrayList<String> initialPositions) {
        this.sizeOfMission = sizeOfMission;
        this.initialPositions = initialPositions;
    }

    public int getSizeOfMission() {
        return sizeOfMission;
    }

    public ArrayList<String> getInitialPositions() {
        return initialPositions;
    }
}
