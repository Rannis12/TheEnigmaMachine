package dtos;

public class TeamInformationDTO {
    private final String allieName;
    private final int amountOfAgent;
    private final int missionsSize;

    public TeamInformationDTO(String allieName, int amountOfAgent, int missionsSize) {
        this.allieName = allieName;
        this.amountOfAgent = amountOfAgent;
        this.missionsSize = missionsSize;
    }

    public String getAllieName() {
        return allieName;
    }

    public int getAmountOfAgent() {
        return amountOfAgent;
    }

    public int getMissionsSize() {
        return missionsSize;
    }
}
