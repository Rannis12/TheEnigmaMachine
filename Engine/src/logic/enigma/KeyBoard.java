package logic.enigma;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KeyBoard implements Serializable {
    private String alphaBet;
    private Map<Integer,Character> rowToChar;
    private Map<Character,Integer> charToRow;

    KeyBoard(String alphaBetFromCTE) {
        rowToChar = new HashMap<>();
        charToRow = new HashMap<>();

        alphaBet = alphaBetFromCTE;
        alphaBet = alphaBet.trim();

        alphaBet.toUpperCase();

        for (int i = 0; i < alphaBet.length(); i++) {
            if(! charToRow.containsKey(alphaBet.charAt(i))) {
                charToRow.put(alphaBet.charAt(i), i + 1);
                rowToChar.put(i + 1, alphaBet.charAt(i));
            }
        }
    }

    public int getRowNum(char ch) {
        return charToRow.get(ch);
    }
    public char getCharFromRow(int val) {
        return rowToChar.get(val);
    }
    public int getAmountOfLetters() {
        return rowToChar.size();
    }


    public boolean isExist(char ch){
        return (charToRow.containsKey(ch));
    }
}
