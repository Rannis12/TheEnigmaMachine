/*
package decryption.dm;

import decryption.DecryptTask;
import decryption.Permuter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import logic.enigma.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Producer extends Task {
    private BlockingQueue<Runnable> blockingQueue;
    private int missionSize;
    private int amountOfRotors;
    private Engine producerEngine;
    private String alphaBet;
    private long startTime;

    private DoubleProperty createdSoFar;
    private DoubleProperty amountOfMissions;
    private String decryptionSelection; //isn't initialized. //todo

    public Producer(int amountOfRotors, String alphaBet, BlockingQueue<Runnable> blockingQueue,
                    Engine copyEngine, int missionSize) {
        this.amountOfRotors = amountOfRotors;
        this.alphaBet = alphaBet;
        this.missionSize = missionSize;
        this.blockingQueue = blockingQueue;
        this.producerEngine = copyEngine;

        this.createdSoFar = new SimpleDoubleProperty();
        this.amountOfMissions = new SimpleDoubleProperty();
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
                amountOfMissions.set(possibleOptions * producerEngine.getEngineFullDetails().getReflectorsAmount());
                break;
            case "Hard":
                amountOfMissions.set(possibleOptions * producerEngine.getAllExistingReflectors().size()
                        * fact(producerEngine.getEngineFullDetails().getUsedAmountOfRotors()));
                break;
            case "Insane":
                amountOfMissions.set(possibleOptions * producerEngine.getAllExistingReflectors().size()
                        * fact(producerEngine.getEngineFullDetails().getUsedAmountOfRotors()) *
                        nCr(producerEngine.getEngineFullDetails().getRotorsAmount(), producerEngine.getEngineFullDetails().getUsedAmountOfRotors()));
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


        startTime = System.nanoTime();
        switch (decryptionSelection) {
            case "Easy":
                easy(producerEngine.getEngineFullDetails().getRotorsCurrentPositions(),
                        producerEngine.getSelectedReflector(),
                        producerEngine.getRotorsIndexes());
                break;
            case "Medium":
                medium(producerEngine.getRotorsIndexes());
                break;
            case "Hard":
                hard(producerEngine.getRotorsIndexes());
                break;
            case "Impossible":
                impossible();
                break;
        }

//        totalTimeTF.setText(totalTimeResult() + " Sec");

    }
    private void easy(String initRotorsPositions, int chosenReflector, ArrayList<Integer> rotorsIndexes){
        producerEngine.initSelectedReflector(chosenReflector);
        producerEngine.initRotorIndexes(rotorsIndexes);

        String initString = "";

        for (int j = 0; j < amountOfRotors; j++) {
            initString += alphaBet.charAt(0);
        }

        producerEngine.initRotorsPositions(initString);

        for (int i = 0; i < calculateOptionOnePossibleMissions() && */
/*!isStopBtnClicked.get()*//*
; i++) {
//            isPauseRunningTask(); //checks if user clicked "pause";

            ArrayList<String> initialPositions = new ArrayList<>();

            for (int k = 0; k < missionSize; k++) { //alwayes be >=0, and if it 0, the condition won't occur.
                initialPositions.add(initString);
                //SteerRotors...
                producerEngine.steerRotors();
                initString = producerEngine.getEngineFullDetails().getRotorsCurrentPositions();

            }
            try {
                blockingQueue.put(new DecryptTask((Engine)producerEngine.clone(), sizeOfMission
                        , toEncode, blockingQueueResponses,
                        initialPositions));
                createdSoFar.set(createdSoFar.get() + 1);
                updateProgress(createdSoFar.get(), amountOfMissions.get());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private void medium(ArrayList<Integer> rotorsIndexes){
        ArrayList<Integer> reflectorsArray = producerEngine.getAllExistingReflectors();

        for (Integer reflector : reflectorsArray) {
            easy(producerEngine.getEngineFullDetails().getRotorsCurrentPositions(),
                    reflector, rotorsIndexes);
        }
    }
    private void hard(ArrayList<Integer> possibleRotorsIndexes) {
        ArrayList<ArrayList<Integer>> possiblePositions = getAllPossibleRotorsPosition(possibleRotorsIndexes);
        for (ArrayList<Integer> rotorsPossiblePosition : possiblePositions) {
            medium(rotorsPossiblePosition);
        }
    }
    private void impossible(){
        List<ArrayList<Integer>> combinations = generate(producerEngine.getEngineFullDetails().getRotorsAmount(),
                producerEngine.getEngineFullDetails().getUsedAmountOfRotors());

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


    */
/*private void isPauseRunningTask(){
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
    }*//*

    @Override
    protected Object call() throws Exception {
        createMissions();
        return 0;
    }
}*/
