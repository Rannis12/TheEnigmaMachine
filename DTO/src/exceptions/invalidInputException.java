package exceptions;

import java.io.Serializable;

public class invalidInputException extends Exception implements Serializable {
    public invalidInputException(String s){
        super(s);
    }
}
