package dtos.engine;

import java.io.Serializable;

public class ReflectorDTO {
    private final int reflectorID;

    public ReflectorDTO(int reflectorID) {
        this.reflectorID = reflectorID - 1;
    }

    public int getReflectorID() {
        return reflectorID;
    }
}
