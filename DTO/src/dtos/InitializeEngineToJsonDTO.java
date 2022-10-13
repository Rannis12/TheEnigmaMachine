package dtos;

public class InitializeEngineToJsonDTO {

    private RotorsFirstPositionDTO rotorsFirstPositionDTO;
    private PlugBoardDTO plugBoardDTO;
    private ReflectorDTO reflectorDTO;
    private RotorsIndexesDTO rotorsIndexesDTO;

    public InitializeEngineToJsonDTO(RotorsFirstPositionDTO rotorsFirstPositionDTO, PlugBoardDTO plugBoardDTO,
                                     ReflectorDTO reflectorDTO, RotorsIndexesDTO rotorsIndexesDTO) {

        this.rotorsFirstPositionDTO = rotorsFirstPositionDTO;
        this.plugBoardDTO = plugBoardDTO;
        this.reflectorDTO = reflectorDTO;
        this.rotorsIndexesDTO = rotorsIndexesDTO;
    }

    public RotorsFirstPositionDTO getRotorsFirstPositionDTO() {
        return rotorsFirstPositionDTO;
    }

    public PlugBoardDTO getPlugBoardDTO() {
        return plugBoardDTO;
    }

    public ReflectorDTO getReflectorDTO() {
        return reflectorDTO;
    }

    public RotorsIndexesDTO getRotorsIndexesDTO() {
        return rotorsIndexesDTO;
    }
}
