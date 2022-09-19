package dtos;

/**
 * This dto will hold all the data about a string that possible to be the origin string.
 */
public class MissionDTO {
    private String agentID;
    private String toEncodeString;
    private String DecodedTo;
    private long timeTaken;

    private String chosenReflector;
//    private String configuration;
    private String rotorsPosition;



    public MissionDTO(String agentID, String toEncodeString, String decodedTo, long timeTaken,
                      String chosenReflector, String rotorsPosition) {
        this.agentID = agentID;
        this.toEncodeString = toEncodeString;
        this.DecodedTo = decodedTo;
        this.timeTaken = timeTaken;
        this.chosenReflector = chosenReflector;
//        this. configuration = configuration;
        this.rotorsPosition = rotorsPosition;
    }

    public String getAgentID() {
        return agentID;
    }

    public String getToEncodeString() {
        return toEncodeString;
    }

    public String getDecodedTo() {
        return DecodedTo;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public String getChosenReflector() {
        return chosenReflector;
    }

/*    public String getConfiguration() {
        return configuration;
    }*/

    public String getRotorsPosition() {
        return rotorsPosition;
    }
}
