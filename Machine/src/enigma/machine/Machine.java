package enigma.machine;

import dtos.EngineFullDetailsDTO;
import dtos.EngineMinimalDetailsDTO;
import dtos.ConfigurationStatisticsDTO;
import dtos.MachineStatisticsDTO;

public interface Machine {
    MachineStatisticsDTO getMachineStatistics();
    EngineFullDetailsDTO getEngineFullDetails();

    EngineMinimalDetailsDTO getEngineMinimalDetails();

    void resetStatistics();
}
