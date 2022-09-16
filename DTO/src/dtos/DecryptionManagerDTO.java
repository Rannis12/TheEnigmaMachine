package dtos;

public class DecryptionManagerDTO {

    //int queueMaxSize, int amountOfRotors, String alphaBet
    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
//    private String selectedRotors;
//    private int selectedReflector;
//    private String notchPositions;
    private String decryptionSelection;


    public DecryptionManagerDTO(int amountOfAgents, int sizeOfMission, String toEncode,
                                /*String selectedRotors,*/  /*String notchPositions,*/
                                String decryptionSelection){
        this.amountOfAgents = amountOfAgents;
        this.sizeOfMission = sizeOfMission;
        this.toEncode = toEncode;
//        this.selectedRotors = selectedRotors;
//        this.selectedReflector = selectedReflector;
//        this.notchPositions = notchPositions;
        this.decryptionSelection = decryptionSelection;
    }

    public String getDecryptionSelection() {
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

//    public String getSelectedRotors() {
//        return selectedRotors;
//    }

//    public int getSelectedReflector() {
//        return selectedReflector;
//    }

    /*public String getNotchPositions() {
        return notchPositions;
    }*/


}
