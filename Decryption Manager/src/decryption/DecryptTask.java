package decryption;

import dtos.DecodeStringInfo;
import dtos.MissionDTO;
import exceptions.invalidInputException;
import logic.enigma.Dictionary;
import logic.enigma.Engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;

import static decryption.dm.DecryptionManager.loadEnigmaFromString;

/**
 * This is what the agent supposed to do - Agent's Job.
 * He gets a string, remove all the forbidden characters from it - in case there is,
 * and encoding the result string.
 * If it looks like the origin possible string (The dictionary will decide it),
 * then he will Create MissionDTO - and insert it to blockingQueueResponses.
 */
public class DecryptTask implements Runnable {
    private Engine engine;
    private BlockingQueue blockingQueueResponses;
    private NewDictionary dictionary;

    private String excludedCharacters;
    private String toEncodeString;
    private int sizeOfMission;

    private String initConfiguration;
//    private int decryptModeOption; //the specific mode to do in run -  from easy mode -> bruteForce mode.


    public DecryptTask(int sizeOfMission, NewDictionary dictionary,/*int decryptModeOption,*/String toEncodeString,
    BlockingQueue blockingQueueResponses){

        this.sizeOfMission = sizeOfMission;
        this.toEncodeString = toEncodeString;
        this.dictionary = dictionary;
        this.excludedCharacters = dictionary.getExcludedCharacters();
        this.blockingQueueResponses = blockingQueueResponses;
//        this.initConfiguration = initConfiguration;

        try {
            engine = loadEnigmaFromString();
        } catch (invalidInputException e) {
            throw new RuntimeException(e);
        }

    }

/*    private static Engine loadEnigmaFromFile() throws invalidInputException {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml"))) {
            Engine engine = (Engine) in.readObject();
            return engine;
        } catch (IOException | ClassNotFoundException ex) {
            throw new invalidInputException("Error in loading a file\n");
        }

    }*/

    /**In here the Agent should try to decode the String that the ThreadPool gave him.
     * Pay attention!
     * if mission's size is 4, then the Agent should do the WHOLE options.
     * for example, if the Rotors first positions is A,A,A ,
     * then the agent should do A,A,E , A,A,F, A,A,G , A,A,H - so each time he will start from different configuration,
     * in order to cover all possible cases.
     *
     */
    @Override
    public void run(){
        try {
            long start = System.nanoTime();
            int numOfSeperates = 0;
            numOfSeperates = getNumOfSeperates(toEncodeString);

            String[] wordsArr = toEncodeString.split(" ", numOfSeperates + 1);
            for (String string : wordsArr) {
                if(!dictionary.search(string)){
                    return; //supposed to finish this task.
                }
            }

            //if we got here, all the Strings in wordsArr in the dictionary. which means toEncodeString is a possible string to encode.

            DecodeStringInfo decodeStringInfo = engine.decodeStr(toEncodeString);
            String resultString = decodeStringInfo.getDecodedString();
            numOfSeperates = getNumOfSeperates(resultString);

            String[] resultWordsArr = resultString.split(" ", numOfSeperates + 1);
            for (String string : resultWordsArr) {
                if(!dictionary.search(string)){
                    return; //supposed to finish this task.
                }
            }

            long end = System.nanoTime();
            //if we got here, resultString might be the origin string, then we need to create a dto of it.
            MissionDTO missionDTO = new MissionDTO(Thread.currentThread().getName(), toEncodeString, resultString, end - start);

            blockingQueueResponses.put(missionDTO); //add a listener to this blocking queue from the Application.

        } catch (invalidInputException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private int getNumOfSeperates(String string) {
        int numOfSeperates = 0;
        for (int i = 0; i < excludedCharacters.length(); i++) {//remove all excluded characters
            string = string.replace(String.valueOf(excludedCharacters.charAt(i)), "");
        }
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                numOfSeperates++;
            }
        }
        return numOfSeperates;
    }

}







/*
    public static void main(String[] args) {
        DecryptTask agent = new DecryptTask(1, "ABC");
        System.out.println("sfdsa");
    }*/


