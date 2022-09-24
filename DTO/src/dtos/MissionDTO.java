package dtos;

/**
 * This dto will hold all the data about a string that possible to be the origin string.
 */
public class MissionDTO {
    private String agentID;
    private String DecodedTo;
    private String chosenReflector;
    private String rotorsPosition;
    private String rotorsOrder;


    public MissionDTO(String agentID, String decodedTo,
                      String chosenReflector, String rotorsPosition, String rotorsOrder) {
        this.agentID = agentID;
        this.DecodedTo = decodedTo;
        this.chosenReflector = chosenReflector;
        this.rotorsPosition = rotorsPosition;
        this.rotorsOrder = rotorsOrder;
    }

    public String getAgentID() {
        return agentID;
    }

    public String getDecodedTo() {
        return DecodedTo;
    }

    public String getChosenReflector() {
        return chosenReflector;
    }

    public String getRotorsPosition() {
        return rotorsPosition;
    }

    public String getRotorsOrder() {
        return rotorsOrder;
    }
}
