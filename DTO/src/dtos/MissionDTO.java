package dtos;

/**
 * This dto will hold all the data about a string that possible to be the origin string.
 */
public class MissionDTO {
    private String agentID;
    private String toEncodeString;
    private String DecodedTo;
    private long timeTaken;

    public MissionDTO(String agentID, String toEncodeString, String decodedTo, long timeTaken) {
        this.agentID = agentID;
        this.toEncodeString = toEncodeString;
        this.DecodedTo = decodedTo;
        this.timeTaken = timeTaken;
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
}
