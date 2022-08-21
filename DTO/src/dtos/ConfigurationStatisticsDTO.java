package dtos;

import java.io.Serializable;
import java.util.ArrayList;

public class ConfigurationStatisticsDTO implements Serializable {

    private final String configurationStr;
    private ArrayList<DecodeStringInfo> infoArray;

    public ConfigurationStatisticsDTO(final String configurationInit){

        infoArray = new ArrayList<>();
        configurationStr = configurationInit;
    }


    public ArrayList<DecodeStringInfo> getInfoArray() {
        return infoArray;
    }
    public String getConfigurationStr() {
        return configurationStr;
    }



    public void insertInfo(DecodeStringInfo newInfo) {
        infoArray.add(newInfo);
    }

}
