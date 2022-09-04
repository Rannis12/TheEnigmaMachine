package decryption;

import logic.enigma.Engine;

/* we need to choose between 2 options:
    1. We can create copies from the engine, and use them in Agent's ctor.
    (Maybe create a method that returns copy of the Engine. )

    2. We can save the Engine to xml file, and loads it into each Agent. (sounds little bit easier.)
* */


public class Agent extends Thread{

    private Engine engine;
    private int agentID;
    private String toDecodeString;







    public Agent(int agentID, String toDecodeString){

        this.agentID = agentID;
        this.toDecodeString = toDecodeString;
    }


    @Override
    public void run(){

    }
}
