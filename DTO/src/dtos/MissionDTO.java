package dtos;

import java.util.ArrayList;

/**
 * This dto will hold all the data about a string that possible to be the origin string.
 */
public class MissionDTO {
    private String agentID;
    private String DecodedTo;
    private String chosenReflector;
    private String rotorsPosition;
    private String rotorsPositionsAndOrder;

    public MissionDTO(String agentID, String decodedTo, String chosenReflector, String rotorsPosition, ArrayList<Integer> rotorsOrganization) {
        this.rotorsPositionsAndOrder = "";
        for (int i = 0; i < rotorsOrganization.size(); i++) {
            if(i == rotorsOrganization.size() - 1)
                this.rotorsPositionsAndOrder = this.rotorsPositionsAndOrder + rotorsOrganization.get(i)  + "(" + rotorsPosition.charAt(i) + ")";
            else
                this.rotorsPositionsAndOrder = this.rotorsPositionsAndOrder + rotorsOrganization.get(i)  + "(" + rotorsPosition.charAt(i) + "),";

        }
        this.agentID = agentID;
        this.DecodedTo = decodedTo;
        this.chosenReflector = chosenReflector;
        this.rotorsPosition = rotorsPosition;
    }
    public String getRotorsPositionsAndOrder() {
        return rotorsPositionsAndOrder;
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
}
