package decryption;

import dtos.DecodeStringInfo;
import dtos.MissionDTO;
import exceptions.invalidInputException;
import logic.enigma.Dictionary;
import logic.enigma.Engine;


import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

//import static decryption.dm.DecryptionManager.loadEnigmaFromString;

/**
 * This is what the agent supposed to do - Agent's Job.
 * He gets a string, remove all the forbidden characters from it - in case there is,
 * and encoding the result string.
 * If it looks like the origin possible string (The dictionary will decide it),
 * then he will Create MissionDTO - and insert it to blockingQueueResponses.
 */
public class DecryptTask implements Runnable {
    private Engine engine;
    private BlockingQueue<MissionDTO> blockingQueueResponses;
    private Dictionary dictionary;
    private String excludedCharacters;
    private String toEncodeString;
    private int sizeOfMission;
    private ArrayList<String> initialPositions;


    public DecryptTask(Engine copyEngine, int sizeOfMission,String currentConfiguration,
                       String toEncodeString, BlockingQueue blockingQueueResponses, ArrayList<String> initialPositions){

        engine = copyEngine;

        this.sizeOfMission = sizeOfMission;
        this.toEncodeString = toEncodeString;
        this.dictionary = engine.getDictionary();
        this.excludedCharacters = dictionary.getExcludedCharacters();
        this.blockingQueueResponses = blockingQueueResponses;

        this.initialPositions = new ArrayList<>();
        for (String config : initialPositions) {
          this.initialPositions.add(config);
        }

//        this.initConfiguration = initConfiguration;
    }

    /**In here the Agent should try to decode the String that the ThreadPool gave him.
     * Pay attention!
     * if mission's size is 4, then the Agent should do the WHOLE options.
     * for example, if the Rotors first positions is A,A,A ,
     * then the agent should do A,A,E , A,A,F, A,A,G , A,A,H - so each time he will start from different configuration,
     * in order to cover all possible cases.
     *
     */

    //the decryption here is without plugboard! therefor, we need to create a new method (not decodeStr),
    //or add boolean value inside this method.
    @Override
    public void run(){
        try {
            for (int i = 0; i < sizeOfMission; i++) {
                boolean shouldContinueSearching = true;

                String initPos = initialPositions.get(initialPositions.size() - 1 - i);
                engine.initRotorsPositions(initPos);

                long start = System.nanoTime();
                DecodeStringInfo decodeStringInfo = engine.decodeStrWithoutPG(toEncodeString);

                String resultString = decodeStringInfo.getDecodedString();

                System.out.println(Thread.currentThread().getName() + ": " + toEncodeString + " -> " + resultString +
                        " with " + initPos + " " + engine.getEngineFullDetails().getChosenReflector());

                int numOfSeparates = getNumOfSeparates(resultString);
                String[] resultWordsArr = resultString.split(" ", numOfSeparates + 1);

                //after split by spaces, we clean each char that is excluded.
                /*for (int j = 0; j < resultWordsArr.length; j++) {
                    for (int k = 0; k < excludedCharacters.length(); k++) {
                        resultWordsArr[j] = resultWordsArr[j].replace(String.valueOf(excludedCharacters.charAt(k)), "");
                    }
                }*/

                for (int t = 0; t < resultWordsArr.length && shouldContinueSearching; t++) {
                    if (!dictionary.isExistInDictionary(resultWordsArr[t])) {
                        shouldContinueSearching = false;
                    }
                }

                System.out.println(shouldContinueSearching);

                if(shouldContinueSearching) {
                    long end = System.nanoTime();
                    //if we got here, resultString might be the origin string, then we need to create a dto of it.
                    System.out.println(Thread.currentThread().getName() + "found that " + toEncodeString + " might be: "
                                        + resultString + " with "+ initPos);

                    MissionDTO missionDTO = new MissionDTO(Thread.currentThread().getName(), toEncodeString,
                            resultString, end - start, engine.getEngineFullDetails().getChosenReflector(),
                            initPos);

                    blockingQueueResponses.put(missionDTO); //add a listener to this blocking queue from the Application.
                }
            }

        } catch (invalidInputException | InterruptedException e) {
            System.out.println("exception in decrypt task");
        }
    }


    private int getNumOfSeparates(String string) {
        int numOfSeparates = 0;
        for (int i = 0; i < excludedCharacters.length(); i++) {//remove all excluded characters
            string = string.replace(String.valueOf(excludedCharacters.charAt(i)), "");
        }
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                numOfSeparates++;
            }
        }
        return numOfSeparates;
    }

}


