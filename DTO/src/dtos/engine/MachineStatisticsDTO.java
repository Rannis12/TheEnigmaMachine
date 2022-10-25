package dtos.engine;

import dtos.ConfigurationStatisticsDTO;
import dtos.DecodeStringInfo;
import exceptions.invalidInputException;

import java.io.Serializable;
import java.util.ArrayList;

public class MachineStatisticsDTO implements Serializable {
    private ArrayList<ConfigurationStatisticsDTO> statisticsArrayList;

    public MachineStatisticsDTO() {
        statisticsArrayList = new ArrayList<>();
    }

    public ArrayList<ConfigurationStatisticsDTO> getStatisticsArrayList() {
        return statisticsArrayList;
    }


    public void addConfiguration(ConfigurationStatisticsDTO configurationStatisticsDTO) {
        statisticsArrayList.add(configurationStatisticsDTO);
    }
    public void addDecodedStringInfo(DecodeStringInfo decodedStringInfo) throws invalidInputException {

        int size = statisticsArrayList.size() ;
        if(size < 1) {
            throw new invalidInputException("Please define machine first!");
        }
        statisticsArrayList.get(size - 1).insertInfo(decodedStringInfo);
    }


    public void resetStatistics() {
        statisticsArrayList.clear();
    }
}
