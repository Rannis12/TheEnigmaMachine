package dtos.entities;

import java.util.Set;

public class AllieDTO {

//    private String username; //maybe we don't need username.
    private String allieName;

    public AllieDTO(String allieName) {
        this.allieName = allieName;
    }

//    public String getUsername() {
//        return username;
//    }

    public String getAllieName() {
        return allieName;
    }
}
