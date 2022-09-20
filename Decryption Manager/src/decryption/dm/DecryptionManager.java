package decryption.dm;


import decryption.DecryptTask;
import decryption.Permuter;
import dtos.DecryptionManagerDTO;
import dtos.MissionDTO;
import dtos.ProgressUpdateDTO;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import logic.enigma.Dictionary;
import logic.enigma.Engine;
import logic.enigma.Rotors;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DecryptionManager {
    private Engine engine;
    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
    private Dictionary dictionary; //we maybe would like to sent him from the controllers, after we open the file.
    private ThreadPoolExecutor threadPool;
    String decryptionSelection;
    BlockingQueue<MissionDTO> blockingQueueResponses; //queue of optional decoded strings.
    private final int QUEUE_MAX_SIZE = 1000; //should be 1000

    private Producer producer;
    private Consumer<MissionDTO> dmConsumer;
    private Consumer<ProgressUpdateDTO> dmProgressConsumer;
    private DoubleProperty checking;
    private DoubleProperty amountOfMissions;

    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO, Engine engineCopy,
                             Consumer<MissionDTO> consumer, Consumer<ProgressUpdateDTO> progressConsumer) throws invalidInputException {
        dmConsumer = consumer;
        dmProgressConsumer = progressConsumer;

        engine = engineCopy; //engineCopy is a copy of engine. I implemented Cloneable.

        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
        toEncode = decryptionManagerDTO.getToEncode();
        decryptionSelection = decryptionManagerDTO.getDecryptionSelection();
        this.dictionary = engine.getDictionary();

        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

        producer = new Producer(engine.getEngineFullDetails().getUsedAmountOfRotors(),
                                engine.getKeyBoard().getAlphaBet(),
                                blockingQueue, (Engine)engine.clone());

        threadPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, 20, TimeUnit.SECONDS,
                blockingQueue,
                new CustomThreadFactory());

        blockingQueueResponses = new LinkedBlockingQueue();
        setThreadOfResponses();
        setProgressBarUpdating();

        checking = new SimpleDoubleProperty();
        amountOfMissions = new SimpleDoubleProperty();

        threadPool.prestartAllCoreThreads();
    }

    //important!! need to fix those methods.
    private void setProgressBarUpdating() {
        new Thread(() -> {
            while(true){
                dmProgressConsumer.accept(new ProgressUpdateDTO(checking, amountOfMissions));
            }
        }).start();

    }
    private void setThreadOfResponses() {
        new Thread(() -> {
            MissionDTO missionDTO = null;
            while(true){
                try {
                    missionDTO = blockingQueueResponses.take();
                    dmConsumer.accept(missionDTO);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }


    private class Producer extends Task {
        private BlockingQueue<Runnable> blockingQueue;
        private int missionSize;
        private int numOfSteers = 0;
        private int amountOfRotors;
        private Engine producerEngine;
        private String alphaBet;

        public Producer(int amountOfRotors, String alphaBet, BlockingQueue<Runnable> blockingQueue,
                        Engine copyEngine) {
            this.amountOfRotors = amountOfRotors;
            this.alphaBet = alphaBet;
            this.missionSize = sizeOfMission;
            this.blockingQueue = blockingQueue;
            this.producerEngine = copyEngine;

        }


        //this should be the "inside method" alwayes gets rotorsOrder, reflector, and configuration.

        /**
         * we should steer the rotors 'missionSize' times. so, if we will call this method,
         * the current rotors positions should be right, and then we can convert the ArrayList
         * of the positions to String, and insert it to the blockingQueue.
         */
       /* private void option1(Engine copyEngine) {  //need to count the number of possible options (possible mission) instead of the doubleLoop.
            new Thread(() -> {
                try{
                    producerEngine = copyEngine;
                    String initString = "";

                    for (int j = 0; j < amountOfRotors; j++) {
                        initString += alphaBet.charAt(0);
                    }

                    producerEngine.initRotorsPositions(initString); //this should be the right method.
                    //calculates amount of possible missions, when given a specific size.

                    //for example, initialize the initString to - "AAAAAAAA".
                    for (int i = 0; i < calculateOptionOnePossibleMissions(); i++) {

                        ArrayList<String> initialPositions = new ArrayList<>();

                        for (int k = 0; k < missionSize; k++) { //alwayes be >=0, and if it 0, the condition won't occur.
                            initialPositions.add(initString);
                            //SteerRotors...
                            producerEngine.steerRotors(); //this method should steer *all rotors* - in case the first gets to the end.
                            initString = producerEngine.getEngineFullDetails().getRotorsCurrentPositions();
                            checking.setValue(checking.getValue() + 1);
                        }

                        for (String string:initialPositions) {
                            System.out.println(string);
                        }

                        blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission,
                                producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses,
                                initialPositions));

                    }

                }catch (InterruptedException e) {
                    System.out.println("Exception in thread that push to queue.");
                }
            }).start();
        }*/

        private int calculateOptionOnePossibleMissions(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;
            return (int)possibleOptions;
        }


        private void createMissions(){
            switch (decryptionSelection) {
                case "Easy":
                    amountOfMissions.setValue(calculateOptionOnePossibleMissions());
                    easy(engine.getEngineFullDetails().getRotorsCurrentPositions(),
                            engine.getSelectedReflector(),
                            engine.getRotorsIndexes());
                    break;
                case "Medium":
                    medium(engine.getRotorsIndexes());
                    break;
                case "Hard":
                    hard(engine.getRotorsIndexes());
                    break;
                case "Impossible":
                    impossible();
                    break;
            }
        }
        private void easy(String initRotorsPositions, int chosenReflector, ArrayList<Integer> rotorsIndexes){
            producerEngine.initSelectedReflector(chosenReflector);
//            producerEngine.initRotorsPositions(initRotorsPositions);
            producerEngine.initRotorIndexes(rotorsIndexes);

            String initString = "";

            for (int j = 0; j < amountOfRotors; j++) {
                initString += alphaBet.charAt(0);
            }

            engine.initRotorsPositions(initString); //this should be the right method.
            //calculates amount of possible missions, when given a specific size.

            //for example, initialize the initString to - "AAAAAAAA".
            for (int i = 0; i < calculateOptionOnePossibleMissions(); i++) {

                ArrayList<String> initialPositions = new ArrayList<>();

                for (int k = 0; k < missionSize; k++) { //alwayes be >=0, and if it 0, the condition won't occur.
                    initialPositions.add(initString);
                    //SteerRotors...
                    producerEngine.steerRotors(); //this method should steer *all rotors* - in case the first gets to the end.
                    initString = producerEngine.getEngineFullDetails().getRotorsCurrentPositions();
                    checking.setValue(checking.getValue());
                }

                for (String string:initialPositions) {
                    System.out.println(string);
                }

                try {
                    blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission,
                            producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses,
                            initialPositions));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        private void medium(ArrayList<Integer> rotorsIndexes){
            ArrayList<Integer> reflectorsArray = engine.getAllExistingReflectors();
            amountOfMissions.setValue(calculateOptionOnePossibleMissions() * reflectorsArray.size());
            for (Integer reflector : reflectorsArray) {
                engine.initSelectedReflector(reflector);
                easy(engine.getEngineFullDetails().getRotorsCurrentPositions(),
                        engine.getSelectedReflector(),
                        rotorsIndexes);
            }
        }
        private void hard(ArrayList<Integer> possibleRotorsIndexes) { // rotors should be instead of engine.gerRotorsIndexes();

            ArrayList<ArrayList<Integer>> possiblePositions = getAllPossibleRotorsPosition(possibleRotorsIndexes);
            for (ArrayList<Integer> rotorsPossiblePosition : possiblePositions) {
                medium(rotorsPossiblePosition);
            }
        }
        private void impossible(){
            List<ArrayList<Integer>> combinations = generate(engine.getEngineFullDetails().getRotorsAmount(),
                    engine.getEngineFullDetails().getUsedAmountOfRotors());
            for (int i = 0; i < combinations.size(); i++) {
                hard(combinations.get(i));
            }
        }

        private ArrayList<ArrayList<Integer>> getAllPossibleRotorsPosition(ArrayList<Integer> rotorsIndexes) { //need to fix it.
            ArrayList<ArrayList<Integer>> possiblePositions = new ArrayList<>();
            Permuter permuter = new Permuter(rotorsIndexes.size());
            int[] indexesArr;

            while((indexesArr = permuter.getNext()) != null){
                ArrayList<Integer> possiblePos = new ArrayList<>(rotorsIndexes.size());
                for (int j = 0; j < rotorsIndexes.size(); j++) {
                    possiblePos.add(0);
                } //init possiblePos arr
                for (int j = 0; j < rotorsIndexes.size(); j++) {
                    possiblePos.set(j, rotorsIndexes.get(indexesArr[j]));
                }
                possiblePositions.add(possiblePos);
            }

            return possiblePositions;
        }
        public List<ArrayList<Integer>> generate(int n, int r) {
            List<ArrayList<Integer>> combinations = new ArrayList<>();
            int[] combination = new int[r];

            // initialize with lowest lexicographic combination
            for (int i = 0; i < r; i++) {
                combination[i] = i;
            }

            while (combination[r - 1] < n) {
                ArrayList<Integer> tmp = new ArrayList<>(r);
                for (int i = 0; i < r; i++) {
                    tmp.add(combination[i] + 1);
                }
                combinations.add(tmp);

                // generate next combination in lexicographic order
                int t = r - 1;
                while (t != 0 && combination[t] == n - r + t) {
                    t--;
                }
                combination[t]++;
                for (int i = t + 1; i < r; i++) {
                    combination[i] = combination[i - 1] + 1;
                }
            }

            return combinations;
        }

        @Override
        protected Object call() throws Exception {
            createMissions();
            return 0; //??
        }
    }

    public void encode() {
        checking.setValue(0); //in order to initialize the progress bar.

        Thread thread = new Thread(producer);
        thread.start();
    }
    public boolean isLegalString(String stringFromUser) throws invalidInputException {

        int numOfSeparates = getNumOfSeparates(stringFromUser);

        String[] wordsArr = stringFromUser.split(" ", numOfSeparates + 1);
        for (String string : wordsArr) {
            if(!dictionary.isExistInDictionary(string)){
                throw new invalidInputException("The string " + string + " isn't in the dictionary!");
            }
        }
        return true;
    }

    private int getNumOfSeparates(String string) {
        int numOfSeperates = 0;
        for (int i = 0; i < dictionary.getExcludedCharacters().length(); i++) {//remove all excluded characters
            string = string.replace(String.valueOf(dictionary.getExcludedCharacters().charAt(i)), "");
        }
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                numOfSeperates++;
            }
        }
        return numOfSeperates;
    }

  /*  public static void main(String[] args){
*//*
        int r = 3;
        int n = 5;
        List<ArrayList<Integer>> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            ArrayList<Integer> tmp = new ArrayList<>(r);
            for (int i = 0; i < r; i++) {
                tmp.add(combination[i]);
            }
            combinations.add(tmp);

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }*//*
        System.out.println("check");
    }*/
}