package logic.enigma;

import exceptions.invalidInputException;
import resources.jaxb.generated.CTERotor;

import java.io.Serializable;
import java.util.*;

public class Rotors implements Serializable {
    private ArrayList<Rotor> rotors;
    private ArrayList<Integer> rotorsIndexes;
    private int usedRotors;
    private String rotorsFirstPositionsString;

    Rotors(List<CTERotor> CTERotor, int rotorCountFromCTE){

        rotors = new ArrayList<>(CTERotor.size());
        rotorsIndexes = new ArrayList<>();
        usedRotors = rotorCountFromCTE;

        for (CTERotor cteRotor: CTERotor) {
            rotors.add(new Rotor(cteRotor));
        }
        Comparator <Rotor> sortByID = (r1, r2) ->  r1.getRotorID() - r2.getRotorID();//sort Rotor by id
        rotors.sort(sortByID);
    }

    public void initRotorsFirstPositions(String rotorsFirstPositionsString){
        this.rotorsFirstPositionsString = rotorsFirstPositionsString;

        for (int i = 0; i < rotorsFirstPositionsString.length(); i++) {
            rotors.get(rotorsIndexes.get(i) - 1).initRotor(rotorsFirstPositionsString.charAt(i));

            rotors.get(rotorsIndexes.get(i) - 1).notchFirstPos = rotors.get(rotorsIndexes.get(i) - 1).getNotchCurrentPlace();

            rotors.get(rotorsIndexes.get(i) - 1).rotorFirstPosition = rotorsFirstPositionsString.charAt(i);
        }
    }
    public void initRotorsIndexes(ArrayList<Integer> uiRotorsIndexes) {
        rotorsIndexes.clear();

        for (int tmpInt : uiRotorsIndexes) {
            rotorsIndexes.add(tmpInt);
        }
    }
    private void initRandomFirstPositions(KeyBoard keyBoard) {
        rotorsFirstPositionsString = "";
        String tmp = "";

        for (int i = 0; i < usedRotors; i++) {
            tmp += rotors.get(rotorsIndexes.get(i) - 1).initRandomFirstPosition(keyBoard);
        }

        rotorsFirstPositionsString = tmp;
    }


    public String getRotorsCurrentPositions(){
        String rotorsCurrentPositions = "";
        for (int i = 0; i < usedRotors; i++) {
            rotorsCurrentPositions += rotors.get(rotorsIndexes.get(i) - 1).rotorCurrentPlace();
        }
        return rotorsCurrentPositions;
    }
    public ArrayList<Integer> getNotchesFirstPositions(){
        ArrayList<Integer> notchesFirstPlaces = new ArrayList<>();
        for (int i = 0; i < usedRotors; i++) {
            notchesFirstPlaces.add(rotors.get(rotorsIndexes.get(i) - 1).getNotchFirstPos());
        }
        return notchesFirstPlaces;
    }
    public ArrayList<Integer> getNotchesCurrentPlaces() {
        ArrayList<Integer> notchesCurrentPlaces = new ArrayList<>();
        for (int i = 0; i < usedRotors; i++) {
            notchesCurrentPlaces.add(rotors.get(rotorsIndexes.get(i) - 1).getNotchCurrentPlace());
        }
        return notchesCurrentPlaces;
    }
    public String getRotorsFirstPositionsString() {
        return rotorsFirstPositionsString;
    }
    public int getRotorsAmount() {
        return rotors.size();
    }
    public int getUsedRotorsAmount() {
        return usedRotors;
    }
    public ArrayList<Integer> getUsedRotorsOrganization() {
        ArrayList<Integer> usedRotorsOrganization = new ArrayList<>();

        for (int i = 0; i < usedRotors; i++) {
            usedRotorsOrganization.add(rotors.get(rotorsIndexes.get(i) - 1).rotorID);
        }
        return usedRotorsOrganization;
    }



    public void randomRotorsConfiguration(KeyBoard keyBoard) {
        rotorsIndexes.clear();

        Random random = new Random();
        ArrayList<Integer>arrRandoms = new ArrayList<>();

        for (int i = 0; i < rotors.size(); i++) {
            arrRandoms.add(-1);
        }
        for (int i = 0; i < usedRotors; i++) {
            int randInt = random.nextInt(rotors.size()) + 1;

            if(arrRandoms.get(randInt - 1) != randInt) {
                rotorsIndexes.add(randInt);
                arrRandoms.set(randInt - 1, randInt);
            }
            else
                i--;
        }

        initRandomFirstPositions(keyBoard);
    }
    public void resetRotorsFirstPositions() {
        for (int i = 0; i < usedRotors; i++) {
            char currRotorFirstPosition = rotors.get(rotorsIndexes.get(i) - 1).rotorFirstPosition;

            rotors.get(rotorsIndexes.get(i) - 1).initRotor(currRotorFirstPosition);

        }
    }
    public int encodeFromRightToLeft(int rowNum){
        int size = rotorsIndexes.size();
        int currRowNum = rowNum;

        for (int i = size - 1; i >= 0; i--) {

            if(i == size -1) {//rightest rotor
                rotors.get(rotorsIndexes.get(i) - 1).steerRotor();

                for (int j = i; j > 0; j--) {//check notch locations and steer other rotors if needed
                    if(rotors.get(rotorsIndexes.get(j) - 1).isFullCycle()){
                        rotors.get(rotorsIndexes.get(j-1) - 1).steerRotor();
                    }
                    else
                        break;
                }
            }
            currRowNum = rotors.get(rotorsIndexes.get(i) - 1).getRowNumberFromRightToLeft(currRowNum);//inside rotor
        }
        return currRowNum;
    }
    public int encodeFromLeftToRight(int rowNum){
        int size = rotorsIndexes.size();
        int currRowNum = rowNum;

        for (int i = 0; i < size; i++) {
            currRowNum = rotors.get(rotorsIndexes.get(i) - 1).getRowNumberFromLeftToRight(currRowNum);
        }

        return currRowNum;
    }
    public void checkRotorIndexesValidity(String rotorsPosition, ArrayList<Integer> uiRotorsIndexes) throws invalidInputException {
        int numOfSeprates = 0;
        for (int i = 0; i < rotorsPosition.length(); i++) {
            if(rotorsPosition.charAt(i) == ','){
                numOfSeprates++;
            }
        }
        if(numOfSeprates + 1 != usedRotors) {//amount of rotors doesn't fit XML file
            uiRotorsIndexes.clear();
            throw new invalidInputException("Error- choose exactly " + usedRotors + " rotors as given in XML file");
        }

        String[] arrOfStr = rotorsPosition.split(",", numOfSeprates + 1);

        for (int i = 0; i < arrOfStr.length; i++) {
            boolean isValid = false;
            int rotorID = Integer.valueOf(arrOfStr[i]);
            for (int j = 0; j < rotors.size() && !isValid; j++) { //check if current rotor exists in rotors array.
                if(rotors.get(j).getRotorID() == rotorID){
                    for (int k = 0; k < uiRotorsIndexes.size(); k++) { //check if current rotor used twice.
                        if(uiRotorsIndexes.get(k) == rotorID){
                            uiRotorsIndexes.clear();
                            throw new invalidInputException("The rotor " + rotorID + " already exists in the machine. Please try again.");
                        }
                    }
                    isValid = true;
                    uiRotorsIndexes.add(rotorID);
                }
            }
            if (isValid == false){
                uiRotorsIndexes.clear();
                throw new invalidInputException("Rotor's ID doesn't exists in xml file. Please try again.");
            }
        }

    }
    public void checkRotorsFirstPositionsValidity(String firstPositions, KeyBoard keyBoard) throws invalidInputException {
        if(usedRotors != firstPositions.length()) {
            throw new invalidInputException("First positions rotors input should be in length " + usedRotors);
        }
        for (int i = 0; i < firstPositions.length(); i++) {
            if(!keyBoard.isExist(firstPositions.charAt(i))) {
                throw new invalidInputException("The Character " + firstPositions.charAt(i) +
                        " isn't exists in the keyboard. Please try again.");
            }
        }
    }




    private class Rotor implements Serializable{
        private LinkedList<Positioning> wiring;
        private Positioning notch;
        private int notchFirstPos;
        private int rotorID;
        private char rotorFirstPosition;

        Rotor(CTERotor cteRotor){
            wiring = new LinkedList<>();

            for (int i = 0; i < cteRotor.getCTEPositioning().size(); i++) {

                char right = cteRotor.getCTEPositioning().get(i).getRight().charAt(0);
                char left = cteRotor.getCTEPositioning().get(i).getLeft().charAt(0);

                if (i+1 == cteRotor.getNotch()){
                    this.notch = new Positioning(left, right);
                }

                wiring.addLast(new Positioning(left, right));
            }
            this.rotorID = cteRotor.getId();
        }
        private char initRandomFirstPosition(KeyBoard keyBoard){
            int letterAmount = keyBoard.getAmountOfLetters();
            Random random = new Random();

            int rand = random.nextInt(letterAmount) + 1;
            rotorFirstPosition = keyBoard.getCharFromRow(rand);

            initRotor(rotorFirstPosition);

            notchFirstPos = getNotchCurrentPlace();

            return rotorFirstPosition;
        }
        public void initRotor(char wanted){
            while (wiring.getFirst().getRight() != wanted){
                wiring.addLast(wiring.removeFirst());
            }
        }



        public int getRotorID() {
            return rotorID;
        }
        public int getRowNumberFromRightToLeft(int currRowNum) {
            int count =1;
            char tmp = wiring.get(currRowNum - 1).getRight();
            boolean isEqual = false;

            for (int i = 0; i < wiring.size() && !isEqual; i++) {
                if(wiring.get(i).getLeft() != tmp) {
                    count++;
                }
                else
                    isEqual = true;
            }
            return count;
        }
        private int getRowNumberFromLeftToRight(int currRowNum) {
            int count =1;
            char tmp = wiring.get(currRowNum - 1).getLeft();
            boolean isEqual = false;

            for (int i = 0; i < wiring.size() && !isEqual; i++) {
                if(wiring.get(i).getRight() != tmp) {
                    count++;
                }
                else
                    isEqual = true;
            }
            return count;
        }
        private int getNotchCurrentPlace() {
            int count = 0;
            boolean foundNotch = false;
            for (int i = 0; i < wiring.size() && !foundNotch ; i++) {
                if(wiring.get(i).getRight() != notch.getRight()) {
                    count++;
                }
                else
                    foundNotch = true;
            }
            return count;
        }
        private int getNotchFirstPos(){
            return notchFirstPos;
        }



        private char rotorCurrentPlace(){
            return wiring.getFirst().getRight();
        }
        public boolean isFullCycle(){
            if (wiring.getFirst().getLeft() == notch.getLeft() &&
                    wiring.getFirst().getRight() == notch.getRight()){
                return true;
            }
            else
                return false;
        }
        public void steerRotor(){
            wiring.addLast(wiring.removeFirst());
        }
    }

    /**
     * this method should steer all the rotors (in case the rightest rotor finished full cycle.)
     * this method is written for the DM.
     */
    public void steerRotors(){
        int size = rotorsIndexes.size();
        rotors.get(rotorsIndexes.get(size - 1) - 1).steerRotor();

        for (int j = size - 1; j > 0; j--) {//check notch locations and steer other rotors if needed
            if(rotors.get(rotorsIndexes.get(j) - 1).isFullCycle()){
                rotors.get(rotorsIndexes.get(j-1) - 1).steerRotor();
            }
            else
                break;
        }
    }
}