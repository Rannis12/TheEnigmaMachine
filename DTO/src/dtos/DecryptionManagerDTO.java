package dtos;

public class DecryptionManagerDTO {

    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
    private String selectedRotors;
    private int selectedReflector;
    private String notchPositions;
    private int decryptionSelection;


    public DecryptionManagerDTO(int amountOfAgents, int sizeOfMission, String toEncode,
                                String selectedRotors, int selectedReflector, String notchPositions,
                                int decryptionSelection){
        this.amountOfAgents = amountOfAgents;
        this.sizeOfMission = sizeOfMission;
        this.toEncode = toEncode;
        this.selectedRotors = selectedRotors;
        this.selectedReflector = selectedReflector;
        this.notchPositions = notchPositions;
        this.decryptionSelection = decryptionSelection;
    }

    public int getDecryptionSelection() {
        return decryptionSelection;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public int getSizeOfMission() {
        return sizeOfMission;
    }

    public String getToEncode() {
        return toEncode;
    }

    public String getSelectedRotors() {
        return selectedRotors;
    }

    public int getSelectedReflector() {
        return selectedReflector;
    }

    public String getNotchPositions() {
        return notchPositions;
    }


}
