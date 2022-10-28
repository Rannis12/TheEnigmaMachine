package dtos.web;

public class ContestProgressDTO {
    private final int totalAmountOfMissions;
    private final int missionsInQueue;
    private final int totalMissionsFinished;

    public ContestProgressDTO(int totalAmountOfMissions, int missionsInQueue, int totalMissionsFinished) {
        this.totalAmountOfMissions = totalAmountOfMissions;
        this.missionsInQueue = missionsInQueue;
        this.totalMissionsFinished = totalMissionsFinished;
    }

    public int getTotalAmountOfMissions() {
        return totalAmountOfMissions;
    }

    public int getMissionsInQueue() {
        return missionsInQueue;
    }

    public int getTotalMissionsFinished() {
        return totalMissionsFinished;
    }
}
