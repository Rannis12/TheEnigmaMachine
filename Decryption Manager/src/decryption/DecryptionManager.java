package decryption;

import dtos.DecryptionManagerDTO;
import logic.enigma.Engine;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DecryptionManager {

    private Engine engine;
    private int amountOfAgents;
    private int sizeOfMission; // amount of missions that each agent need to do
    private String toEncode;
    private ExecutorService threadPool;

    DecryptionSelection decryptionSelection;

    private enum DecryptionSelection {

        OPTION_ONE,
        OPTION_TWO,
        OPTION_THREE,
        BRUTE_FORCE;

        private DecryptionSelection convertToEnum(int selection){
            switch (selection){
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


    public DecryptionManager(DecryptionManagerDTO decryptionManagerDTO){
        amountOfAgents = decryptionManagerDTO.getAmountOfAgents();
        sizeOfMission = decryptionManagerDTO.getSizeOfMission();
        toEncode = decryptionManagerDTO.getToEncode();

        decryptionSelection = decryptionSelection.convertToEnum(decryptionManagerDTO.getDecryptionSelection());



    }


    public void encode(){
        switch (decryptionSelection){
            case OPTION_ONE:
                encodeWhenOnlyMissingPosition();
            case OPTION_TWO:
                encodeWhenPositionsAndReflectorMissing();
            case OPTION_THREE:
                encodeWhenOnlyRotorsAreKnown();
            case BRUTE_FORCE:
                bruteForce();
        }
    }

    public void encodeWhenOnlyMissingPosition(){
        threadPool = Executors.newFixedThreadPool(amountOfAgents);
        ArrayList<Agent> agentArrayList = new ArrayList<>(amountOfAgents);

        for (int i = 0; i < amountOfAgents; i++) {
            threadPool.execute(agentArrayList.get(i));
        }
        threadPool.shutdown();
    }

    public void encodeWhenPositionsAndReflectorMissing(){

    }

    public void encodeWhenOnlyRotorsAreKnown(){

    }

    public void bruteForce(){

    }




}
