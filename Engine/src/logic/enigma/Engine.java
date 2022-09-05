package logic.enigma;

import dtos.*;
import enigma.machine.Machine;
import exceptions.invalidInputException;
import resources.jaxb.generated.*;

import javax.lang.model.type.ArrayType;
import java.io.Serializable;
import java.util.*;

public class Engine implements Serializable, Machine {
    private Reflectors reflectors;
    private Rotors rotors;
    private PlugBoard plugBoard;
    private KeyBoard keyBoard;
    private MachineStatisticsDTO machineStatisticsDTO;
    private int amountOfDecodedStrings;
    private Dictionary dictionary;

    //we need to use it. in Agent im calling this whole path, instead calling LOADED_ENGINE_PATH.
    public final String LOADED_ENGINE_PATH = "C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml";
    Engine(List <CTEReflector> cteReflectors, List<CTERotor> cteRotors, int rotorCountFromCTE, String alphaBetFromCTE, CTEDictionary cteDictionary) {

        keyBoard = new KeyBoard(alphaBetFromCTE);
        dictionary = new Dictionary(keyBoard, cteDictionary);

        reflectors = new Reflectors(cteReflectors);
        rotors = new Rotors(cteRotors, rotorCountFromCTE);
        plugBoard = new PlugBoard();
        machineStatisticsDTO = new MachineStatisticsDTO();

        amountOfDecodedStrings = 0;
    }




    public void initRotorIndexes(ArrayList<Integer> uiRotorsIndexes) {
        rotors.initRotorsIndexes(uiRotorsIndexes);
    }
    public void initRotorsFirstPositions(String rotorsFirstPositionsString) {
        rotors.initRotorsFirstPositions(rotorsFirstPositionsString);
    }
    public void initSelectedReflector(int requestedReflector) {
        reflectors.initReflectorFromUser(requestedReflector);
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

    public ArrayList<Integer> getReflectorsIDs(){
        return reflectors.getReflectorsIDs();
    }

    @Override
    public MachineStatisticsDTO getMachineStatistics() {
        return machineStatisticsDTO;
    }
    @Override
    public EngineFullDetailsDTO getEngineFullDetails() {
        int usedAmountOfRotors = rotors.getUsedRotorsAmount();
        int rotorsAmount = rotors.getRotorsAmount();
        int reflectorsAmount = reflectors.getAmountOfReflectors();

        String plugBoardString = plugBoard.getPlugBoardPairs();
        String chosenReflector = reflectors.getReflectorID();
        String rotorsFirstPositions = rotors.getRotorsFirstPositionsString();
        String rotorsCurrentPositions = rotors.getRotorsCurrentPositions();

        ArrayList<Integer> usedRotorsOrganization = rotors.getUsedRotorsOrganization();
        ArrayList<Integer> notchesCurrentPlaces = rotors.getNotchesCurrentPlaces();
        ArrayList<Integer> notchesFirstPlaces = rotors.getNotchesFirstPositions();

        return new EngineFullDetailsDTO(usedAmountOfRotors, rotorsAmount, reflectorsAmount, amountOfDecodedStrings, plugBoardString,
                chosenReflector, rotorsFirstPositions, usedRotorsOrganization, notchesCurrentPlaces, notchesFirstPlaces, rotorsCurrentPositions);
    }
    @Override
    public EngineMinimalDetailsDTO getEngineMinimalDetails() {
        int usedAmountOfRotors = rotors.getUsedRotorsAmount();
        int rotorsAmount = rotors.getRotorsAmount();
        int reflectorsAmount = reflectors.getAmountOfReflectors();

        return new EngineMinimalDetailsDTO(usedAmountOfRotors, rotorsAmount, reflectorsAmount, amountOfDecodedStrings);
    }
    @Override
    public void resetStatistics() {
        machineStatisticsDTO.resetStatistics();
        amountOfDecodedStrings = 0;
    }
}



