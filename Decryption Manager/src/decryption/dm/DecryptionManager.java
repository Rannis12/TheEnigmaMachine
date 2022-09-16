package decryption.dm;

import decryption.DecryptTask;
import dtos.DecryptionManagerDTO;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;

import logic.enigma.Dictionary;
import logic.enigma.Engine;

import java.util.concurrent.*;

public class DecryptionManager {
    private Engine engine; //ok
    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
    private Dictionary dictionary; //we maybe would like to sent him from the controllers, after we open the file.
    private ThreadPoolExecutor threadPool; //ok
    String decryptionSelection;
    BlockingQueue blockingQueueResponses; //queue of optional decoded strings.
    CustomThreadFactory customThreadFactory;
    private Producer producer; // not sure if leave it here as a nested Class or created a new class.

/*    private enum DecryptionSelection {

        OPTION_ONE,
        OPTION_TWO,
        OPTION_THREE,
        BRUTE_FORCE;

        private DecryptionSelection convertToEnum(String selection) {
            switch (selection) {
                case "Easy":
                    return OPTION_ONE;
                case "Medium":
                    return OPTION_TWO;
                case "Hard":
                    return OPTION_THREE;
                case "Impossible":
                    return BRUTE_FORCE;
            }
            return null;
        }
    }*/

    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO, Engine engineCopy) throws invalidInputException {
        engine = engineCopy; //engineCopy is a copy of engine. I implemented Cloneable.

        engine.initPlugBoardForDM();

        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
        toEncode = decryptionManagerDTO.getToEncode();

        decryptionSelection = decryptionManagerDTO.getDecryptionSelection();

        this.dictionary = engine.getDictionary();
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(1000);

        producer = new Producer(engine.getEngineFullDetails().getUsedAmountOfRotors(),
                                engine.getKeyBoard().getAlphaBet(),
                                blockingQueue);

        customThreadFactory = new CustomThreadFactory();

        threadPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, 20, TimeUnit.SECONDS,
                blockingQueue,
                customThreadFactory);

        blockingQueueResponses = new LinkedBlockingQueue();

        threadPool.prestartAllCoreThreads();
    }

    private class Producer {
        private BlockingQueue<Runnable> blockingQueue;
        private final int QUEUE_MAX_SIZE = 1000; //should be 1000
        private int missionSize; //checking.
        private int numOfSteers = 0;
        private int amountOfRotors;
        private Engine producerEngine;
        private String alphaBet;
        private String currentConfiguration; // in case we will return "create missions" , we would like to know where we stopped.

        public Producer(int amountOfRotors, String alphaBet, BlockingQueue<Runnable> blockingQueue) {
            this.amountOfRotors = amountOfRotors;
            this.alphaBet = alphaBet;
            this.currentConfiguration = "";
            this.missionSize = sizeOfMission;
            this.blockingQueue = blockingQueue;
            this.producerEngine = (Engine)engine.clone();
        }


        /**
         * we should steer the rotors 'missionSize' times. so, if we will call this method,
         * the current rotors positions should be right, and then we can convert the ArrayList
         * of the positions to String, and insert it to the blockingQueue.
         */
        private void option1() {  //need to count the number of possible options (possible mission) instead of the doubleLoop.
            new Thread(() -> {
                try{
                    String initString = "";

                    for (int j = 0; j < amountOfRotors; j++) {
                        initString += alphaBet.charAt(0);
                    }
                    System.out.println(initString);

                    producerEngine.initRotorsFirstPositions(initString); //this should be the right method.

                    //calculates amount of possible missions, when given a specific size.
                    double possibleOptions = calculateOptionOnePossibleMissions();

                    //for example, initialize the initString to - "AAAAAAAA".
                    for (int i = 0; i < possibleOptions; i++) {

                        if (i == 0){ //doing it since we don't want to miss the first initialization.
                            System.out.println("inserted " + producerEngine.getEngineFullDetails().getRotorsCurrentPositions() + " configuration.");
                            blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission, /*selection,*/
                                    producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses));
                        }
                        for (int k = 0; k < missionSize; k++) {
                            //SteerRotors...
                            producerEngine.steerRotors(); //this method should steer *all rotors* - in case the first gets to the end.
                            numOfSteers++;
                        }

                        blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission, /*selection,*/
                                producerEngine.getEngineFullDetails().getRotorsCurrentPositions(), toEncode, blockingQueueResponses));
                        System.out.println("inserted " + producerEngine.getEngineFullDetails().getRotorsCurrentPositions() + " configuration.");
                    }

                    currentConfiguration = producerEngine.getEngineFullDetails().getRotorsCurrentPositions(); //necessary??

                }catch (InterruptedException e) {
                    System.out.println("Exception in thread that push to queue.");
                }
            }).start();
        }

        /*public BlockingQueue<Runnable> setBlockingQueue() {
            return blockingQueue;
        }*/

        private int calculateOptionOnePossibleMissions(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;
            return (int)possibleOptions;
        }

    }

    public void encode() {
        switch (decryptionSelection) {
            case "Easy":
                encodeWhenOnlyMissingPosition();
                break;
            case "Medium":
                //for each reflector, we need to set him in the engine, and then decode all options.
                //engine.setReflector()
//                for (int i = 0; i < ; i++) {
//
//                }
                break;
            case "Hard":
                encodeWhenOnlyRotorsAreKnown();
                break;
            case "Impossible":
                impossible();
                break;
        }
    }


    /**
     * option 1 in brute force.
     */
    public void encodeWhenOnlyMissingPosition() { //probably not good.
        producer.option1();
    }


    /**
     * option 2 in brute force.
     */
    public void encodeWhenPositionsAndReflectorMissing() {

    }

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

    /*public static void saveEnigmaToString(Engine engine) throws invalidInputException {
        try
        {
            // To String
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(engine);
            oos.close();
            serializedEngine = Base64.getEncoder().encodeToString(baos.toByteArray());

            System.out.println("saved successful\n");


        } catch(IOException e) {
            throw new invalidInputException("Error in saving a file\n");
        }
    }

    public static Engine loadEnigmaFromString() throws invalidInputException {
        try {
            byte[] data = Base64.getDecoder().decode(serializedEngine);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Engine engine =(Engine) ois.readObject();
            ois.close();
            System.out.println("loaded successfully\n");
            return engine;

        }catch (IOException | ClassNotFoundException ex) {
            throw new invalidInputException("Error in loading a file\n");
        }
    }*/


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


