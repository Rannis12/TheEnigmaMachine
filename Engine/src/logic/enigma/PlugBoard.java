package logic.enigma;

import dtos.engine.PlugBoardDTO;
import exceptions.invalidInputException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlugBoard implements Serializable {
    private Map<Character, Character> cables;
    private String initString;

    PlugBoard(){
        cables = new HashMap<>();
    }
    public void initFromUser(PlugBoardDTO plugBoardDTO){
        cables.clear();
        initString = "";

        initString = plugBoardDTO.getInitString();
        cables.putAll(plugBoardDTO.getUICables());
    }
    public char getConnection(char input){
        if(isExist(input)){
            return cables.get(input);
        }
        return input;
    }
    public String getPlugBoardPairs() {
        return plugBoardPrintFormat();
    }
    private String plugBoardPrintFormat() {
        String cpyString = new String(initString);
        int size = cpyString.length();
        String newString = "";

        for (int i = 0; i < size; i += 2) {
            newString += cpyString.charAt(i);
            newString += '|';
            newString += cpyString.charAt(i + 1);
            if(i != size - 2){
                newString += ',';
            }
        }
        return newString;
    }
    public void randomPlugBoard(KeyBoard keyBoard) {
        Random random = new Random();
        cables.clear();
        initString = "";
        int letters = keyBoard.getAmountOfLetters();

        int amountOfPlugs = random.nextInt(letters / 2) + 1;
        for (int i = 0; i < amountOfPlugs; i++) {

            char left,right;
            int tmpLeft, tmpRight;

            tmpLeft = random.nextInt(letters) + 1;
            tmpRight = random.nextInt(letters) + 1;
            while(tmpLeft == tmpRight) {//make sure the letters are different
                tmpRight = random.nextInt(letters) + 1;
            }

            left = keyBoard.getCharFromRow(tmpLeft);
            right = keyBoard.getCharFromRow(tmpRight);
            while(isExist(left)) {
                tmpLeft = random.nextInt(letters) + 1;
                left = keyBoard.getCharFromRow(tmpLeft);
            }
            while(isExist(right) || (right == left)) {
                tmpRight = random.nextInt(letters) + 1;
                right = keyBoard.getCharFromRow(tmpRight);
            }
            cables.put(left, right);
            cables.put(right, left);

            initString = initString + left + right;
        }
    }
    private boolean isExist(char input){
        return cables.containsKey(input);
    }
    public void checkPlugBoardStringValidity(String plugBoardString, KeyBoard keyBoard, Map<Character, Character> uiCables) throws invalidInputException {
        uiCables.clear();

        if(!(plugBoardString.length() % 2 == 0)){
            throw new invalidInputException("Input doesn't contains even amount of letters.");
        }
        else{
            for (int i = 0; i < plugBoardString.length(); i+=2) {
                char left = plugBoardString.charAt(i);
                char right = plugBoardString.charAt(i+1);

                if(keyBoard.isExist(left) && keyBoard.isExist(right)) {
                    if (left == right) {//same letter with means no cable needed
                        throw new invalidInputException("Trying to map the letter " + left + " to itself");
                    }
                    else if(!uiCables.containsKey(left) && !uiCables.containsKey(right)) {
                        uiCables.put(left, right);
                        uiCables.put(right, left);
                    }
                    else {
                        if(uiCables.containsKey(left))
                            throw new invalidInputException("Trying to initialize the letter " + left + " twice.");
                        if(uiCables.containsKey(right))
                            throw new invalidInputException("Trying to initialize the letter " + right + " twice.");
                    }
                }
                else {
                    if(!keyBoard.isExist(left)) {
                        throw new invalidInputException("Letter " + left + " doesn't exist in keyBoard");
                    }
                    if(!keyBoard.isExist(right)) {
                        throw new invalidInputException("Letter " + right + " doesn't exist in keyBoard");
                    }
                }
            }
        }
    }

    public void initForDM() {
        cables = new HashMap<>();
        initString = "";
    }
}
