package logic.enigma;

import exceptions.invalidInputException;
import resources.jaxb.generated.CTEReflect;
import resources.jaxb.generated.CTEReflector;

import java.io.Serializable;
import java.util.*;

public class Reflectors implements Serializable {
    private int requestedReflector;//assumption - range 0-4
    private Map <Integer,Reflector> reflectorsMap;
    Reflectors(List<CTEReflector> cteReflectors) {

        reflectorsMap = new HashMap<>();

        for (int i = 0; i < cteReflectors.size(); i++) {
            Reflector newRef = new Reflector(cteReflectors.get(i));

            switch (cteReflectors.get(i).getId()) {
                case "I":
                    reflectorsMap.put(0,newRef);
                    break;
                case "II":
                    reflectorsMap.put(1,newRef);
                    break;
                case "III":
                    reflectorsMap.put(2,newRef);
                    break;
                case "IV":
                    reflectorsMap.put(3,newRef);
                    break;
                case "V":
                    reflectorsMap.put(4,newRef);
                    break;
            }
        }
    }

    public void initReflectorFromUser(int requestedReflector) {
        this.requestedReflector = requestedReflector;
    }



    public int getConnection(int val) {
        return reflectorsMap.get(requestedReflector).getConnection(val);
    }
    public int getAmountOfReflectors(){
        return reflectorsMap.size();
    }
    public String getReflectorID(){
        return reflectorsMap.get(requestedReflector).getID();
    }



    public void checkReflectorFromUserValidity(String reflectorID) throws invalidInputException {
        try {
            int tmpRequestedReflector = Integer.parseInt(reflectorID) - 1;
            if(!reflectorsMap.containsKey(tmpRequestedReflector)){
                throw new invalidInputException("Reflector ID doesn't exists in xml file. Please try again.");
            }
        }catch(NumberFormatException e){
            throw new invalidInputException("Invalid input. Please try again.");
        }
    }
    public void randomReflector() {
        Random random = new Random();
        int rand = random.nextInt(5);
        while(! reflectorsMap.containsKey(rand)) {
            rand = random.nextInt(5);
        }
        requestedReflector = rand;
    }

    /*public ArrayList<Integer> getReflectorsIDs(){
        ArrayList<Integer> arrOfIDs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(reflectorsMap.containsKey(i)){
                arrOfIDs.add(i + 1);
            }
        }
        return arrOfIDs;
    }*/////////////////////////////////////////////////////////////////////////////////////
    public int getReflectorsIDs(){
        int amount = 0;

        for (int i = 0; i < 5; i++) {
            if(reflectorsMap.containsKey(i)){
                amount++;
            }
        }
        return amount;
    }

    private class Reflector implements Serializable{
        private Map<Integer,Integer> wiring;
        private String ID;

        Reflector(CTEReflector cteReflector) {
            wiring = new HashMap<>();

            for (CTEReflect tmp : cteReflector.getCTEReflect()) {
                wiring.put(tmp.getInput(), tmp.getOutput());
                wiring.put(tmp.getOutput() ,tmp.getInput());
            }

            ID = cteReflector.getId();
        }


        public int getConnection(int val) {
            return  (wiring.get(val));
        }
        private String getID() {
            return ID;
        }
    }

}
