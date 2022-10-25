package decryption.dm;

import decryption.Permuter;
import dtos.DecryptionManagerDTO;
import dtos.MissionDTO;
import dtos.web.DecryptTaskDTO;
import exceptions.invalidInputException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import logic.enigma.Dictionary;
import logic.enigma.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

public class DecryptionManager {
    public static final Object pauseLock = new Object();
//    private BooleanProperty isStopBtnClicked;

    private Engine engine;
//    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
//    private String toEncode;
    private Dictionary dictionary; //we maybe would like to sent him from the controllers, after we open the file.
//    private ThreadPoolExecutor threadPool;
    private String decryptionSelection;
    BlockingQueue<MissionDTO> blockingQueueResponses; //queue of optional decoded strings.
    private final int QUEUE_MAX_SIZE = 1000;
    public static long startTime; //measuring the time we start create tasks.
    private Producer producer; //creates all the tasks.
//    private Consumer<MissionDTO> dmConsumer;
    private DoubleProperty createdSoFar;

    private DoubleProperty amountOfMissions;

//    private TextField totalTimeTF;
//    private BooleanProperty isSystemPause;

    private BlockingQueue<DecryptTaskDTO> blockingQueue;

    public DecryptionManager(Engine engineAfterClone, String decryptionSelection, int missionSize){

        this.engine = engineAfterClone;

        this.sizeOfMission = missionSize;
        this.decryptionSelection = decryptionSelection;
        this.dictionary = engine.getDictionary();

        this.blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE); //maybe the max size of the queue here isn't 1000?

        producer = new Producer(engine.getEngineFullDetails().getUsedAmountOfRotors(),
                engine.getKeyBoard().getAlphaBet(),
                blockingQueue, (Engine)engine.clone());


/*        threadPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, 20, TimeUnit.SECONDS,
                blockingQueue,
                new CustomThreadFactory());*/

        blockingQueueResponses = new LinkedBlockingQueue();
//        setThreadOfResponses(); //??

        createdSoFar = new SimpleDoubleProperty();
        amountOfMissions = new SimpleDoubleProperty();

//        this.totalTimeTF = totalTimeTF;

//        threadPool.prestartAllCoreThreads(); //makes the threadpool run.
        producer.calculatePossibleMissions(); //calculates the missions.
    }

    //ex.2 ctor - no use in this ctor - delete later.
    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO, Engine engineCopy,
                             Consumer<MissionDTO> consumer,
                             BooleanProperty isSystemPause, BooleanProperty isStopBtnClicked,
                             TextField totalTimeTF) throws invalidInputException {

        /*this.isSystemPause = isSystemPause; //init in method.
        this.isStopBtnClicked = isStopBtnClicked;

        dmConsumer = consumer;


        engine = engineCopy; //engineCopy is a copy of engine. I implemented Cloneable.

        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
//        toEncode = decryptionManagerDTO.getToEncode();
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

        createdSoFar = new SimpleDoubleProperty();
        amountOfMissions = new SimpleDoubleProperty();

        this.totalTimeTF = totalTimeTF;

        threadPool.prestartAllCoreThreads();


        producer.calculatePossibleMissions();*/
    }

   /* public void bindingsToUI(ProgressBar tasksProgressBar, Label percentageLabel) {
        tasksProgressBar.progressProperty().bind(producer.progressProperty());
        percentageLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        producer.progressProperty(),
                                        100)),
                        " %"));


    }
*/
    public DecryptTaskDTO pollOneMission() {
        return blockingQueue.poll();
    }

    /*private void setThreadOfResponses() { // NOT NECESSARY METHOD in ex.3
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

    }*/
    private class Producer implements Runnable {
        private BlockingQueue<DecryptTaskDTO> blockingQueue;
        private int missionSize;
        private int amountOfRotors;
        private Engine producerEngine;
        private String alphaBet;

        public Producer(int amountOfRotors, String alphaBet, BlockingQueue<DecryptTaskDTO> blockingQueue,
                        Engine copyEngine) {
            this.amountOfRotors = amountOfRotors;
            this.alphaBet = alphaBet;
            this.missionSize = sizeOfMission;
            this.blockingQueue = blockingQueue;
            this.producerEngine = copyEngine;

        }
        private int calculateOptionOnePossibleMissions(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;
            return (int)possibleOptions;

        }
        private void calculatePossibleMissions(){ //need to check it
            double alphaBetLength = alphaBet.length();
            double num = Math.pow(alphaBetLength, amountOfRotors); //initialized amount of rotors.
            double possibleOptions = num / missionSize;

            switch(decryptionSelection){
                case "Easy":
                   amountOfMissions.set(possibleOptions);
                   break;
                case "Medium":
                    amountOfMissions.set(possibleOptions * engine.getEngineFullDetails().getReflectorsAmount());
                    break;
                case "Hard":
                    amountOfMissions.set(possibleOptions * engine.getAllExistingReflectors().size()
                            * fact(engine.getEngineFullDetails().getUsedAmountOfRotors()));
                    break;
                case "Insane":
                    amountOfMissions.set(possibleOptions * engine.getAllExistingReflectors().size()
                            * fact(engine.getEngineFullDetails().getUsedAmountOfRotors()) *
                            nCr(engine.getEngineFullDetails().getRotorsAmount(), engine.getEngineFullDetails().getUsedAmountOfRotors()));
                    break;
            }
        }

        private int fact(int n){
            int fact = 1;
            for(int i = 1;i <= n; i++){
                fact = fact * i;
            }
            return fact;
        }
        private int nCr(int n, int r) {
            return fact(n) / (fact(r) *
                              fact(n - r));
        }
        private void createMissions(){
            //in order to initialize the progress bar.
            createdSoFar.set(0);
            System.out.println("inside create missions");

            startTime = System.nanoTime();
            switch (decryptionSelection) {
                case "Easy":
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
                case "Insane":
                    insane();
                    break;
            }

//            totalTimeTF.setText(totalTimeResult() + " Sec");

        }
        private void easy(String initRotorsPositions, int chosenReflector, ArrayList<Integer> rotorsIndexes){
            producerEngine.initSelectedReflector(chosenReflector);
            producerEngine.initRotorIndexes(rotorsIndexes);

            String initString = "";

            for (int j = 0; j < amountOfRotors; j++) {
                initString += alphaBet.charAt(0);
            }

            producerEngine.initRotorsPositions(initString);

            for (int i = 0; i < calculateOptionOnePossibleMissions() /*&& !isStopBtnClicked.get()*/; i++) {
//                isPauseRunningTask(); //checks if user clicked "pause";

                ArrayList<String> initialPositions = new ArrayList<>();

                for (int k = 0; k < missionSize; k++) { //alwayes be >=0, and if it 0, the condition won't occur.
                    initialPositions.add(initString);
                    //SteerRotors...
                    producerEngine.steerRotors();
                    initString = producerEngine.getEngineFullDetails().getRotorsCurrentPositions();

                }
                try {
                    /*blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission
                            , "delete", blockingQueueResponses,
                            initialPositions));*/

                    blockingQueue.put(new DecryptTaskDTO(sizeOfMission, initialPositions));

                    createdSoFar.set(createdSoFar.get() + 1);
//                    updateProgress(createdSoFar.get(), amountOfMissions.get());

                    System.out.println("queue size: " + blockingQueue.size());


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        private void medium(ArrayList<Integer> rotorsIndexes){
            ArrayList<Integer> reflectorsArray = engine.getAllExistingReflectors();

            for (Integer reflector : reflectorsArray) {
                easy(engine.getEngineFullDetails().getRotorsCurrentPositions(),
                        reflector, rotorsIndexes);
            }
        }
        private void hard(ArrayList<Integer> possibleRotorsIndexes) {
            ArrayList<ArrayList<Integer>> possiblePositions = getAllPossibleRotorsPosition(possibleRotorsIndexes);
            for (ArrayList<Integer> rotorsPossiblePosition : possiblePositions) {
                medium(rotorsPossiblePosition);
            }
        }
        private void insane(){
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

            // initialize with the lowest lexicographic combination
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


      /*  private void isPauseRunningTask(){
            if(isSystemPause.get()){
                synchronized (pauseLock){
                    if(isSystemPause.get()){
                        try{
                            pauseLock.wait();
                        }catch (InterruptedException ignored){

                        }
                    }
                }
            }
        }*/

        @Override
        public void run() {
            System.out.println("start create missions");
            createMissions();
        }
    }

    public static String totalTimeResult() {
        long end = System.nanoTime();
        long temp = (end - startTime) /1000000000;
        return String.valueOf(temp);
    }

    public void encode() {
        Thread thread = new Thread(producer);
        thread.start();
    }


   /* public void stopAllAgents(){
        threadPool.shutdownNow();
    }*/

    public int getAmountOfMissions() {
        return (int)amountOfMissions.get();
    }
}
