package logic.enigma;

import java.io.Serializable;

public class Positioning implements Serializable {
    private char left;
    private char right;

    Positioning(char left, char right){
        this.left = left;
        this.right = right;
    }


    public char getLeft() {
        return left;
    }

    public char getRight() {
        return right;
    }
}
