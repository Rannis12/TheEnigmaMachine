package dtos.web;

import java.util.ArrayList;

public class DataToAgentEngineDTO {

    private final ArrayList<Integer> usedRotorsOrganization;
    private final ArrayList<Integer> notchesCurrentPlaces;
    private final String inputStreamAsString;
    private final int selectedReflector;

    public DataToAgentEngineDTO(ArrayList<Integer> usedRotorsOrganization, ArrayList<Integer> notchesCurrentPlaces,
                                String inputStreamAsString, int selectedReflector) {
        this.usedRotorsOrganization = usedRotorsOrganization;
        this.notchesCurrentPlaces = notchesCurrentPlaces;
        this.inputStreamAsString = inputStreamAsString;
        this.selectedReflector = selectedReflector;
    }

    public ArrayList<Integer> getUsedRotorsOrganization() {
        return usedRotorsOrganization;
    }

    public ArrayList<Integer> getNotchesCurrentPlaces() {
        return notchesCurrentPlaces;
    }

    public String getInputStreamAsString() {
        return inputStreamAsString;
    }

    public int getSelectedReflector() {
        return selectedReflector;
    }
}
