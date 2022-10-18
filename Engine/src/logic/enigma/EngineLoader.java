package logic.enigma;

import exceptions.invalidXMLfileException;
import resources.jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public class EngineLoader {
    private String  FileDestination;
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "resources.jaxb.generated";
    public EngineLoader(String fileDestination) {
        this.FileDestination = fileDestination;
    }

    public EngineLoader(){

    }

    public Engine loadEngineFromXml(String filePath) throws invalidXMLfileException {
        CTEEnigma cteEnigma = null;
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            cteEnigma = deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException e) {
            throw new invalidXMLfileException("This File isn't a xml file that fits xsd. Please enter a valid xml file path: ");
        }

        return convertToEngine(cteEnigma);
    }

    public Engine loadEngineFromInputStream(InputStream in) throws invalidXMLfileException {
        CTEEnigma cteEnigma = null;
        try {
            cteEnigma = deserializeFrom(in);

        } catch (JAXBException e) {
            throw new invalidXMLfileException("TO CHECKKKKK"); //todo
        }

        return convertToEngine(cteEnigma);
    }







    public static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    private Engine convertToEngine(CTEEnigma cteEnigma/*, Dictionary dictionary*/) throws invalidXMLfileException {
        Engine newEngine = null;
        String alphaBetFromCTE = cteEnigma.getCTEMachine().getABC();
        alphaBetFromCTE = alphaBetFromCTE.trim();
        int rotorCount = cteEnigma.getCTEMachine().getRotorsCount();

        List<CTEReflector> cteReflectors = cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        List<CTERotor> cteRotorsLIST = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        CTEDecipher cteDecipher = cteEnigma.getCTEDecipher();
        CTEBattlefield CteBattlefield = cteEnigma.getCTEBattlefield();

        if(checkCTEEnigma(alphaBetFromCTE, rotorCount, cteReflectors, cteRotorsLIST, cteDecipher, CteBattlefield) == true) {
            newEngine =  new Engine(cteReflectors, cteRotorsLIST, rotorCount, alphaBetFromCTE/*, cteDecipher.getAgents() */, cteDecipher.getCTEDictionary(), CteBattlefield);
            //dictionary = new Dictionary(newEngine.getKeyBoard(), cteDecipher.getCTEDictionary());
        }
        return newEngine;
    }
    private boolean checkCTEEnigma(String alphaBetFromCTE, int rotorCount, List<CTEReflector> cteReflectorsLIST,
                                   List<CTERotor> cteRotorsLIST, CTEDecipher cteDecipher,
                                   CTEBattlefield cteBattlefield) throws invalidXMLfileException {
        if (alphaBetFromCTE.length() % 2 != 0) {
            throw new invalidXMLfileException("Error in loading a file: Alpha-Bet in file does not even!");
        }
        if (rotorCount > cteRotorsLIST.size()) {
            throw new invalidXMLfileException("Error in loading a file: Rotors count parameter is too high.");
        }
        if (rotorCount < 2) {
            throw new invalidXMLfileException("Error in loading a file: Minimal Enigma machine should have at least 2 rotors and theres only " + rotorCount + " in this file");
        }
        /*if(cteDecipher.getAgents() < 2 || cteDecipher.getAgents() > 50) {
            throw new invalidXMLfileException("Error: agents amount should be between 2 and 50.");
        }*/
        if(cteBattlefield.getAllies() <= 0){
            throw new invalidXMLfileException("Error: amount of allies must be a positive number.");
        }
        if(!checkDifficultyValidation(cteBattlefield.getLevel())){
            throw new invalidXMLfileException("Error: the word " + cteBattlefield.getLevel() + " isn't a possible difficulty selection.");
        }

        boolean isValidRotors = false, isValidPositioning = false, isValidReflectors = false;


        isValidRotors = checkRotors(cteRotorsLIST, alphaBetFromCTE);

        isValidPositioning = checkPositioning(cteRotorsLIST, alphaBetFromCTE);

        isValidReflectors = checkReflectors(cteReflectorsLIST);

        if(isValidRotors && isValidPositioning && isValidReflectors) {
            return true;
        }
        else
            return false;

    }

    private boolean checkRotors(List<CTERotor> cteRotorsLIST, String alphaBetFromCTE) throws invalidXMLfileException {

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < cteRotorsLIST.size(); i++) {
            if (set.contains(cteRotorsLIST.get(i).getId())) {
                throw new invalidXMLfileException("Error in loading a file: Rotor ID " + cteRotorsLIST.get(i).getId() + " occur more then once in file");
            } else
                set.add(cteRotorsLIST.get(i).getId());

            if (cteRotorsLIST.get(i).getNotch() > alphaBetFromCTE.length()) {//check notch validity
                throw new invalidXMLfileException("Error in loading a file: Rotor ID " + cteRotorsLIST.get(i).getId() + " has invalid notch mapping");
            }
        }
        return true;
    }
    private boolean checkPositioning(List<CTERotor> cteRotorsLIST, String alphaBetFromCTE) throws invalidXMLfileException {
        for (int i = 0; i < cteRotorsLIST.size(); i++) {

            Map<String, String> cables = new HashMap<>();

            List<CTEPositioning> ctePositioning = cteRotorsLIST.get(i).getCTEPositioning();
            if(ctePositioning.size() != alphaBetFromCTE.length()){
                throw new invalidXMLfileException("Error in loading a file: Rotor " + cteRotorsLIST.get(i).getId() + " doesn't has " + alphaBetFromCTE.length() + " letters" );
            }
            for (CTEPositioning position : ctePositioning) {
                if (cables.containsKey(position.getRight()) || cables.containsValue(position.getLeft()) || cables.containsValue("") || cables.containsKey("")) {
                    throw new invalidXMLfileException("Error in loading a file: Double mappings inside Rotor ID: " + cteRotorsLIST.get(i).getId());
                } else {
                    cables.put(position.getRight(), position.getLeft());
                }
            }
        }
    return true;
    }
    private boolean checkReflectors(List<CTEReflector> cteReflectorsLIST) throws invalidXMLfileException {

        Map<String, Integer> reflectorID = new HashMap<>();
        for (int i = 0; i < cteReflectorsLIST.size(); i++) {
            CTEReflector reflector = cteReflectorsLIST.get(i);

            Map<Integer, Integer> cables = new HashMap<>();


            for (int j = 0; j < reflector.getCTEReflect().size(); j++) {
                if (reflector.getCTEReflect().get(j).getInput() == reflector.getCTEReflect().get(j).getOutput()) {
                    throw new invalidXMLfileException("Error in loading a file: Reflector ID" + reflector.getId() +
                            "has mapping between letter " + reflector.getCTEReflect().get(j).getInput() + "and itself");
                }
                if(cables.containsKey(reflector.getCTEReflect().get(j).getInput()) || cables.containsKey(reflector.getCTEReflect().get(j).getOutput())) {
                    throw new invalidXMLfileException("Error in loading a file: Double mappings inside Reflector ID "+ reflector.getId());
                }
                else {
                    cables.put(reflector.getCTEReflect().get(j).getInput(),reflector.getCTEReflect().get(j).getOutput());
                    cables.put(reflector.getCTEReflect().get(j).getOutput(), reflector.getCTEReflect().get(j).getInput());
                }


            }
            if (reflectorID.containsKey(cteReflectorsLIST.get(i).getId())) {
                throw new invalidXMLfileException("Error in loading a file: Reflector ID" + cteReflectorsLIST.get(i).getId() + "occur more then once in file ");
            } else {
                switch (cteReflectorsLIST.get(i).getId()) {
                    case "I":
                        reflectorID.put("I", 1);
                        break;
                    case "II":
                        reflectorID.put("II", 2);
                        break;
                    case "III":
                        reflectorID.put("III", 3);
                        break;
                    case "IV":
                        reflectorID.put("IV", 4);
                        break;
                    case "V":
                        reflectorID.put("V", 5);
                        break;
                }

            }
        }
        return true;
    }
    public static boolean isFileExistAndXML(String fullPath) throws invalidXMLfileException {
        if(fullPath.endsWith(".xml")){
            File tmpFile = new File(fullPath);
            if(tmpFile.exists()){
                return true;
            }
            else{
                throw new invalidXMLfileException("File doesn't exists.");
            }
        }
        else{
            throw new invalidXMLfileException(fullPath +" isn't a xml file.");
        }
    }

    //needs to check validation only in xml file, since we supposed to use a combo box in the application,
    public static boolean checkDifficultyValidation(String value){
        boolean isValid = false;
        switch(value){
            case "Easy":
            case "Medium":
            case "Hard":
            case "Insane":
                isValid = true;
                break;
        }
        return isValid;
    }


    public static void main(String[] args) {
        System.out.println(checkDifficultyValidation("Insane1"));
    }
}
