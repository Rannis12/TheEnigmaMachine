package decryption.dm;

import decryption.DecryptTask;
import decryption.NewDictionary;
import dtos.DecryptionManagerDTO;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;

import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import resources.jaxb.generated.CTEDictionary;

import java.io.*;
import java.util.Base64;
import java.util.concurrent.*;

public class DecryptionManager {
    private Engine engine; //ok
    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
    private NewDictionary dictionary; //we maybe would like to sent him from the controllers, after we open the file.
    private ThreadPoolExecutor threadPool; //ok

    DecryptionSelection decryptionSelection; private int selection; //its here since we might remove decryptionSelection.
    BlockingQueue blockingQueueResponses; //queue of optional decoded strings.
    CustomThreadFactory customThreadFactory;

    private Producer producer; // not sure if leave it here as a nested Class or created a new class.


    static String serializedEngine = new String(); //in order to load engines without ctor and without multiply file opening.

    private enum DecryptionSelection {

        OPTION_ONE,
        OPTION_TWO,
        OPTION_THREE,
        BRUTE_FORCE;

        private DecryptionSelection convertToEnum(int selection) {
            switch (selection) {
                case 0:
                    return OPTION_ONE;
                case 1:
                    return OPTION_TWO;
                case 2:
                    return OPTION_THREE;
                case 3:
                    return BRUTE_FORCE;
            }
            return null;
        }
    }

    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO, Engine engine, CTEDictionary cteDictionary) throws invalidInputException {
        saveEnigmaToString(engine);
        this.engine = loadEnigmaFromString();
        this.dictionary = new NewDictionary(engine.getKeyBoard(), cteDictionary);   //Pay Attention to Send HIM!!!!!!!!!!!!

       // producer = new Producer(10, 3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"); //Ran's check

        customThreadFactory = new CustomThreadFactory();

        threadPool = new ThreadPoolExecutor(1, 50, 50, TimeUnit.MILLISECONDS,
                producer.getBlockingQueue(),
                customThreadFactory);

        blockingQueueResponses = new LinkedBlockingQueue();

        threadPool.prestartAllCoreThreads();



        //from here, maybe move it to another method, to avoid hugh c'tor.
        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
        toEncode = decryptionManagerDTO.getToEncode();

        decryptionSelection = decryptionSelection.convertToEnum(decryptionManagerDTO.getDecryptionSelection());
        this.selection = decryptionManagerDTO.getDecryptionSelection();

    }
    private class Producer {
        private BlockingQueue<Runnable> blockingQueue;
        private int queueMaxSize; //should be 1000
        private int missionSize = 1; //checking.
        private int numOfSteers = 0;
        private int amountOfRotors;
        private String alphaBet;
        private String currentConfiguration; // in case we will return "create missions" , we would like to know where we stopped.

        public Producer(int amountOfRotors, String alphaBet) {
            this.queueMaxSize = 1000;
            this.amountOfRotors = amountOfRotors;
            this.alphaBet = alphaBet;
            this.currentConfiguration = "";

            blockingQueue = new LinkedBlockingQueue<>(queueMaxSize);
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

                    for (int j = 0; j < alphaBet.length(); j++) {
                        initString += alphaBet.charAt(0);
                    }

                    //calculates amount of possible missions, when given a specific size.
                    double possibleOptions = calculateOptionOnePossibleMission();

                    //for example, initialize the initString to - "AAAAAAAA".
                    for (int i = 0; i < possibleOptions; i++) {

                        for (int k = 0; k < missionSize; k++) {
                            //SteerRotors...
                            engine.steerRotors(); //this method should steer *all rotors* - in case the first gets to the end.
                            numOfSteers++;
                        }

                        blockingQueue.put(new DecryptTask(sizeOfMission, dictionary, /*selection,*/
                                engine.getEngineFullDetails().getRotorsCurrentPositions(), blockingQueueResponses));
                    }
                }catch (InterruptedException e) {
                    System.out.println("Exception in thread that push to queue.");
                }
            }).start();

            currentConfiguration = engine.getEngineFullDetails().getRotorsCurrentPositions(); //not good place.
        }

        public BlockingQueue<Runnable> getBlockingQueue() {
            return blockingQueue;
        }

        private int calculateOptionOnePossibleMission(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;
            return (int)possibleOptions;
        }

    }
    public void encode() {
        switch (decryptionSelection) {
            case OPTION_ONE:
                encodeWhenOnlyMissingPosition();
            case OPTION_TWO:
                encodeWhenPositionsAndReflectorMissing();
            case OPTION_THREE:
                encodeWhenOnlyRotorsAreKnown();
            case BRUTE_FORCE:
                impossible();
        }
    }


    /**
     * option 1 in brute force.
     */
    public void encodeWhenOnlyMissingPosition() {
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





    public static void saveEnigmaToString(Engine engine) throws invalidInputException {
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
    }


    public static void main(String[] args) throws invalidXMLfileException {

        EngineLoader engineLoader = new EngineLoader("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml");
        Engine engine1 = engineLoader.loadEngineFromXml("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml");

//        DecryptionManager decryptionManager = new DecryptionManager(engine1.getDec, engine1);
    }
}


