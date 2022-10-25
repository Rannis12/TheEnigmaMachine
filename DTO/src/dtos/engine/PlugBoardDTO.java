package dtos.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlugBoardDTO {
    private Map<Character, Character> uiCables;
    private String initString;


    public PlugBoardDTO(){
        uiCables = new HashMap<>();
    }

    public void setInitString(String initString) {
        this.initString = initString;
    }


    public Map<Character, Character> getUICables() {
        return uiCables;
    }
    public String getInitString() {
        return initString;
    }


}
