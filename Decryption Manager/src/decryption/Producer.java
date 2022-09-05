package decryption;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Producer {
    private BlockingQueue blockingQueue;
    private int queueMaxSize;
    private int missionSize;

    private int numOfSteers = 0;
    private int amountOfRotors;
    private String alphaBet;
    private String CurrentConfiguration; // in case we will return "create missions" , we would like to know where we stopped.

    public Producer(int queueMaxSize, int amountOfRotors, String alphaBet){
        this.queueMaxSize = queueMaxSize;
        this.amountOfRotors = amountOfRotors;
        this.alphaBet = alphaBet;

        blockingQueue = new ArrayBlockingQueue(queueMaxSize);
    }

    private void createMissions(){
        String initString = "";

        //for example, initialize the initString to - "AAAAAAAA".
        for (int i = 0; i < queueMaxSize; i++) {

            /*for (int j = 0; j < alphaBet.length(); j++) {
                initString += alphaBet.charAt(0);

            }*/

            /*for (int j = 0; j < queueMaxSize; j += missionSize) {

                if(numOfSteers - alphaBet.length() >= 0){
                    //steerWheels...
                    numOfSteers -= missionSize;
                }*/

                //we should steer the rotors 'missionSize' times. so, if we will call this method,
                //the current rotors positions should be right, and then we can convert the ArrayList
                //of the positions to String, and insert it to the blockingQueue.

            }



        }






        /*blockingQueue.add("AAA");*/
    }

   /* public static void main(String[] args) {
        Producer producer = new Producer(5, 3, "ABC");
        producer.createMissions();
    }*/

