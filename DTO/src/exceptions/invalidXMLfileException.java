package exceptions;

import java.io.Serializable;

public class invalidXMLfileException extends Exception implements Serializable {

    public invalidXMLfileException(String s){
        super(s);
    }
}
