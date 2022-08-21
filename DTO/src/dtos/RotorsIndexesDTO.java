package dtos;

import java.io.Serializable;
import java.util.ArrayList;

public class RotorsIndexesDTO {
    private ArrayList<Integer> UIRotorsIndexes;

    public RotorsIndexesDTO(){
        UIRotorsIndexes = new ArrayList<>();
    }

    public ArrayList<Integer> getUIRotorsIndexes() {
        return UIRotorsIndexes;
    }
}
