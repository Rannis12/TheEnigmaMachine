package decryption.dm;

import decryption.DecryptTask;
import dtos.DecryptionManagerDTO;
import dtos.MissionDTO;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import logic.enigma.Dictionary;
import logic.enigma.Engine;

import java.util.ArrayList;
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
    private Producer producer;
    private Consumer<MissionDTO> dmConsumer;
    private Consumer<Double> dmProgressConsumer;
    private DoubleProperty checking;

    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO, Engine engineCopy,
                             Consumer<MissionDTO> consumer, Consumer<Double> progressConsumer) throws invalidInputException {
        dmConsumer = consumer;
        dmProgressConsumer = progressConsumer;

        engine = engineCopy; //engineCopy is a copy of engine. I implemented Cloneable.
//        engine.initPlugBoardForDM();

        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
        toEncode = decryptionManagerDTO.getToEncode();
        decryptionSelection = decryptionManagerDTO.getDecryptionSelection();
        this.dictionary = engine.getDictionary();

        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(1000);

        producer = new Producer(engine.getEngineFullDetails().getUsedAmountOfRotors(),
                                engine.getKeyBoard().getAlphaBet(),
                                blockingQueue);

        threadPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, 20, TimeUnit.SECONDS,
                blockingQueue,
                new CustomThreadFactory());

        blockingQueueResponses = new LinkedBlockingQueue();
        setThreadOfResponses();
        setProgressBarUpdating();

        checking = new SimpleDoubleProperty();
        checking.set(0);

        threadPool.prestartAllCoreThreads();
    }

    private void setProgressBarUpdating() {
        new Thread(() -> {
            while(true){
                dmProgressConsumer.accept(checking.getValue());
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


    private class Producer {
        private BlockingQueue<Runnable> blockingQueue;
        private final int QUEUE_MAX_SIZE = 1000; //should be 1000
        private int missionSize;
        private int numOfSteers = 0;
        private int amountOfRotors;
        private Engine producerEngine;
        private String alphaBet;

        public Producer(int amountOfRotors, String alphaBet, BlockingQueue<Runnable> blockingQueue) { //engine init occurs in option1.
            this.amountOfRotors = amountOfRotors;
            this.alphaBet = alphaBet;

            this.missionSize = sizeOfMission;
            this.blockingQueue = blockingQueue;

//            this.producerEngine = (Engine)engine.clone();
        }


        /**
         * we should steer the rotors 'missionSize' times. so, if we will call this method,
         * the current rotors positions should be right, and then we can convert the ArrayList
         * of the positions to String, and insert it to the blockingQueue.
         */
        private void option1(Engine copyEngine) {  //need to count the number of possible options (possible mission) instead of the doubleLoop.
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
        }

        private int calculateOptionOnePossibleMissions(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;
            return (int)possibleOptions;
        }

    }

    public void encode() {
        checking.set(0); //in order to initialize the progress bar.
        switch (decryptionSelection) {
            case "Easy": {
                encodeWhenOnlyMissingPosition((Engine) this.engine.clone());
                break;
            }
            case "Medium": {
                ArrayList<Integer> reflectorsArray = engine.getAllExistingReflectors();
//                ArrayList<>
                //for each reflector, we need to set him in the engine, and then decode all options. ----- not working :(
                for (int i = 0; i < reflectorsArray.size(); i++) {
                    engine.initSelectedReflector(reflectorsArray.get(i));
                    encodeWhenOnlyMissingPosition((Engine) this.engine.clone());
                }
                break;
            }
            case "Hard":
//                encodeWhenOnlyRotorsAreKnown();
                break;
            case "Impossible":
//                impossible();
                break;
        }
    }


    /**
     * option 1 in brute force.
     */
    public void encodeWhenOnlyMissingPosition(Engine copyEngine) { //probably not good.
        producer.option1(copyEngine);
    }


    /**
     * option 2 in brute force.
     */
    /*public void encodeWhenPositionsAndReflectorMissing() {

    }*/

    /**
     * option 3 in brute force.
     */
    public void encodeWhenOnlyRotorsAreKnown() {

    }

    /**
     * option 4 in brute force.
     */
    public void impossible() {

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

    public static void main(String[] args) throws invalidXMLfileException {

/*        EngineLoader engineLoader = new EngineLoader("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml");
        Engine engine1 = engineLoader.loadEngineFromXml("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml");*/

//        DecryptionManager decryptionManager = new DecryptionManager(engine1.getDec, engine1);

        /*String alphaBet = "ABCDEF";
        int amountOfRotors = 3;
        int missionSize = 7;

        double alphaBetLength = alphaBet.length();
        double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
        double possibleOptions = num / missionSize;
        System.out.println( possibleOptions);*/
    }
}


//    private void option1() {  //need to count the number of possible options (possible mission) instead of the doubleLoop.
//        new Thread(() -> {
//            try{
//                String initString = "";
//
//                for (int j = 0; j < amountOfRotors; j++) {
//                    initString += alphaBet.charAt(0);
//                }
//
//                System.out.println(initString);
//
//                producerEngine.initRotorsFirstPositions(initString); //this should be the right method.
//
//                //calculates amount of possible missions, when given a specific size.
//                double possibleOptions = calculateOptionOnePossibleMissions();
//
//                //for example, initialize the initString to - "AAAAAAAA".
//                for (int i = 0; i < possibleOptions; i++) {
//
//                    if (i == 0){ //doing it since we don't want to miss the first initialization.
//                        System.out.println("inserted " + producerEngine.getEngineFullDetails().getRotorsCurrentPositions() + " configuration.");
//                        blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission, /*selection,*/
//                                producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses));
//                    }
//                    for (int k = 0; k < missionSize; k++) {
//                        //SteerRotors...
//                        producerEngine.steerRotors(); //this method should steer *all rotors* - in case the first gets to the end.
//                        numOfSteers++;
//                    }
//
//                    blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission, /*selection,*/
//                            producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses));
//                    System.out.println("inserted " + producerEngine.getEngineFullDetails().getRotorsCurrentPositions() + " configuration.");
//                }
//
//                currentConfiguration = producerEngine.getEngineFullDetails().getRotorsCurrentPositions(); //necessary??
//
//            }catch (InterruptedException e) {
//                System.out.println("Exception in thread that push to queue.");
//            }
//        }).start();
//    }

