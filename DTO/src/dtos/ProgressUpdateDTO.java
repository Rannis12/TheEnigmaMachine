package dtos;

import javafx.beans.property.DoubleProperty;

public class ProgressUpdateDTO {
    private DoubleProperty progress;
    private DoubleProperty allPossibleMissions;

    public ProgressUpdateDTO(DoubleProperty progress, DoubleProperty allPossibleMissions) {
        this.progress = progress;
        this.allPossibleMissions = allPossibleMissions;
    }

    public Double getProgress() {
        return progress.getValue();
    }

    public Double getAllPossibleMissions() {
        return allPossibleMissions.getValue();
    }

}
