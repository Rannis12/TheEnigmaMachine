package logic.enigma;

import dtos.*;
import exceptions.invalidInputException;
import resources.jaxb.generated.*;

import java.io.*;
import java.util.*;

public class Engine implements Serializable, Cloneable {
    private Reflectors reflectors;
    private Rotors rotors;
    private PlugBoard plugBoard;
    private KeyBoard keyBoard;
    private MachineStatisticsDTO machineStatisticsDTO;
    private int agentMaxAmount;
    private int amountOfDecodedStrings;
    private Dictionary dictionary;        //engine shouldn't include Dictionary.

    private BattleField battleField;

    //agentMaxAmount field no required anymore.
    Engine(List <CTEReflector> cteReflectors, List<CTERotor> cteRotors, int rotorCountFromCTE,
           String alphaBetFromCTE/*, int agentMaxAmount*/, CTEDictionary cteDictionary) {

        keyBoard = new KeyBoard(alphaBetFromCTE);
        dictionary = new Dictionary(keyBoard, cteDictionary);
        /*this.agentMaxAmount = agentMaxAmount;*/

        reflectors = new Reflectors(cteReflectors);
        rotors = new Rotors(cteRotors, rotorCountFromCTE);
        plugBoard = new PlugBoard();
        machineStatisticsDTO = new MachineStatisticsDTO();

        amountOfDecodedStrings = 0;
    }

    public Engine (){

    }

    @Override
    public Object clone(){
        Engine engineCopy = null;
        try {
            engineCopy = loadEnigmaFromString(saveEnigmaToString(this));
        } catch (invalidInputException e) {
            throw new RuntimeException("error in cloning the engine.");
        }

        return engineCopy;
    }

    public void initRotorIndexes(ArrayList<Integer> uiRotorsIndexes) {
        rotors.initRotorsIndexes(uiRotorsIndexes);
    }
    public void initRotorsFirstPositions(String rotorsFirstPositionsString) {
        rotors.initRotorsFirstPositions(rotorsFirstPositionsString);
    }
    public void initSelectedReflector(int requestedReflector) {
        reflectors.initRequestedReflector(requestedReflector);
    }
    public void initPlugBoard(PlugBoardDTO plugBoardDTO) {
        plugBoard.initFromUser(plugBoardDTO);
    }
    public KeyBoard getKeyBoard() {
        return keyBoard;
    }


    public DecodeStringInfo decodeStr(String toEncodeString) throws invalidInputException {
        toEncodeString = toEncodeString.toUpperCase();
        checkStringValidity(toEncodeString);

        String decodedString = new String();

        long start = System.nanoTime();

        for (int i = 0; i < toEncodeString.length(); i++) {
            decodedString = decodedString +  decodeChar(toEncodeString.charAt(i));
        }
        long end = System.nanoTime();
        amountOfDecodedStrings++;

        return new DecodeStringInfo(toEncodeString, decodedString, end - start);
    }
    public char decodeChar(Character toDecode) {
        int rowNum;
        char newChar;

        newChar = plugBoard.getConnection(toDecode);

        rowNum = keyBoard.getRowNum(newChar);
        rowNum = rotors.encodeFromRightToLeft(rowNum);

        rowNum = reflectors.getConnection(rowNum);

        rowNum = rotors.encodeFromLeftToRight(rowNum);
        newChar = keyBoard.getCharFromRow(rowNum);

        newChar = plugBoard.getConnection(newChar);

        return newChar;
    }

    public void checkRotorIndexesValidity(String rotorsPosition, ArrayList<Integer> uiRotorsIndexes) throws invalidInputException {
        rotors.checkRotorIndexesValidity(rotorsPosition, uiRotorsIndexes);
    }
    private void checkStringValidity(String toEncodeString) throws invalidInputException {
        ArrayList<Character> invalidChars = new ArrayList<>();
        for (int i = 0; i < toEncodeString.length(); i++) {
            if(!keyBoard.isExist(toEncodeString.charAt(i))) {
                if(!invalidChars.contains(toEncodeString.charAt(i))) {
                    invalidChars.add(toEncodeString.charAt(i));
                }
            }
        }

        if(invalidChars.size() >0) {
            String invalidCharacters = "";
            for (int i = 0; i < invalidChars.size(); i++) {
                invalidCharacters = invalidCharacters + invalidChars.get(i) ;
                if(i+1 != invalidChars.size()) {
                    invalidCharacters += ",";
                }
            }
            if(invalidChars.size() == 1)
                throw new invalidInputException("The letter " + invalidCharacters + " doesn't exist in the language. Please try again.\n");
            else
                throw new invalidInputException("The letters " + invalidCharacters + " doesn't exist in the language. Please try again.\n");
        }
    }

    public void checkRotorsFirstPositionsValidity(String firstPositions, KeyBoard keyBoard) throws invalidInputException {
        rotors.checkRotorsFirstPositionsValidity(firstPositions, keyBoard);
    }

    public void checkSelectedReflectorValidity(String reflectorID) throws invalidInputException {
        reflectors.checkReflectorFromUserValidity(reflectorID);
    }

    public void checkPlugBoardValidity(String plugBoardString, Map<Character, Character> uiCables) throws invalidInputException {
        plugBoard.checkPlugBoardStringValidity(plugBoardString, keyBoard, uiCables);
    }



    public void setNewMachine(RotorsFirstPositionDTO rotorsFirstPositionDTO, PlugBoardDTO plugBoardDTO, ReflectorDTO reflectorDTO, RotorsIndexesDTO rotorsIndexesDTO) {

        initRotorIndexes(rotorsIndexesDTO.getUIRotorsIndexes());

        initRotorsFirstPositions(rotorsFirstPositionDTO.getRotorsFirstPosition());

        initSelectedReflector(reflectorDTO.getReflectorID());

        initPlugBoard(plugBoardDTO);

    }
    public void randomEngine(){
        plugBoard.randomPlugBoard(keyBoard);
        rotors.randomRotorsConfiguration(keyBoard);
        reflectors.randomReflector();
    }
    public void resetEngineToUserInitChoice() {
        rotors.resetRotorsFirstPositions();
    }

    public int getReflectorsIDs(){
        return reflectors.getReflectorsIDs();
    }

    public MachineStatisticsDTO getMachineStatistics() {
        return machineStatisticsDTO;
    }
    public EngineFullDetailsDTO getEngineFullDetails() {
        int usedAmountOfRotors = rotors.getUsedRotorsAmount();
        int rotorsAmount = rotors.getRotorsAmount();
        int reflectorsAmount = reflectors.getAmountOfReflectors();

//        String plugBoardString = plugBoard.getPlugBoardPairs();  checking.
        String plugBoardString = null;

        String chosenReflector = reflectors.getReflectorID();
        String rotorsFirstPositions = rotors.getRotorsFirstPositionsString();
        String rotorsCurrentPositions = rotors.getRotorsCurrentPositions();

        ArrayList<Integer> usedRotorsOrganization = rotors.getUsedRotorsOrganization();
        ArrayList<Integer> notchesCurrentPlaces = rotors.getNotchesCurrentPlaces();
        ArrayList<Integer> notchesFirstPlaces = rotors.getNotchesFirstPositions();

        return new EngineFullDetailsDTO(usedAmountOfRotors, rotorsAmount, reflectorsAmount, amountOfDecodedStrings, plugBoardString,
                chosenReflector, rotorsFirstPositions, usedRotorsOrganization, notchesCurrentPlaces, notchesFirstPlaces, rotorsCurrentPositions);
    }
    public EngineMinimalDetailsDTO getEngineMinimalDetails() {
        int usedAmountOfRotors = rotors.getUsedRotorsAmount();
        int rotorsAmount = rotors.getRotorsAmount();
        int reflectorsAmount = reflectors.getAmountOfReflectors();

        return new EngineMinimalDetailsDTO(usedAmountOfRotors, rotorsAmount, reflectorsAmount, amountOfDecodedStrings);
    }

    public int getAgentMaxAmount() {
        return agentMaxAmount;
    }
    public void resetStatistics() {
        machineStatisticsDTO.resetStatistics();
        amountOfDecodedStrings = 0;
    }

    public void steerRotors() {
        rotors.steerRotors();
    }

    public String saveEnigmaToString(Engine engine) throws invalidInputException {
        try
        {
            // To String
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(engine);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch(IOException e) {
            throw new invalidInputException("Error in saving a file\n");
        }
    }

    public Engine loadEnigmaFromString(String s) throws invalidInputException {
        try {
            byte[] data = Base64.getDecoder().decode(s);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Engine engine =(Engine) ois.readObject();
            ois.close();
            return engine;

        }catch (IOException | ClassNotFoundException ex) {
            throw new invalidInputException("Error in loading a file\n");
        }
    }

    public Dictionary getDictionary() {
        return this.dictionary;
    }

    public void initRotorsPositions(String initString) {
        rotors.initRotorsPosition(initString);
    }

    public ArrayList<Integer> getAllExistingReflectors(){
        return reflectors.getAllExistingReflectors();
    }

    public DecodeStringInfo decodeStrWithoutPG(String toEncodeString) throws invalidInputException {
        toEncodeString = toEncodeString.toUpperCase();
        checkStringValidity(toEncodeString);

        String decodedString = new String();

        long start = System.nanoTime();

        for (int i = 0; i < toEncodeString.length(); i++) {
            decodedString = decodedString +  decodeCharWithoutPG(toEncodeString.charAt(i));
        }
        long end = System.nanoTime();
        amountOfDecodedStrings++;

        return new DecodeStringInfo(toEncodeString, decodedString, end - start);
    }

    public char decodeCharWithoutPG(Character toDecode) {
        int rowNum;
        char newChar;

//        newChar = plugBoard.getConnection(toDecode);

        rowNum = keyBoard.getRowNum(toDecode);
        rowNum = rotors.encodeFromRightToLeft(rowNum);

        rowNum = reflectors.getConnection(rowNum);

        rowNum = rotors.encodeFromLeftToRight(rowNum);
        newChar = keyBoard.getCharFromRow(rowNum);

//        newChar = plugBoard.getConnection(newChar);

        return newChar;
    }

    public ArrayList<Integer> getRotorsIndexes(){
        return rotors.getUsedRotorsIndexes();
    }

    public int getSelectedReflector(){
        return reflectors.getSelectedReflector();
    }

/*    public String getRotorsIndexesString() {
        ArrayList<Integer> rotorsIndexes = rotors.getUsedRotorsIndexes();
        String rotorsIndexesString = "";

        for (int i = 0; i < rotorsIndexes.size(); i++) {
            if(i == rotorsIndexes.size() - 1)
                rotorsIndexesString += String.valueOf(rotorsIndexes.get(i));
            else
                rotorsIndexesString += String.valueOf(rotorsIndexes.get(i)) + ',';

        }
        return rotorsIndexesString;
    }*/
}



