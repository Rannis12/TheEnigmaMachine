package decryption;

import exceptions.invalidInputException;
import logic.enigma.Engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

/* we need to choose between 2 options:
    1. We can create copies from the engine, and use them in Agent's ctor.
    (Maybe create a method that returns copy of the Engine. )

    2. We can save the Engine to xml file, and loads it into each Agent. (sounds little bit easier.)
* */


public class Agent implements Runnable{

    private Engine engine;
    private int agentID;
    private String toDecodeString;



    public Agent(int agentID, String toDecodeString){

        this.agentID = agentID;
        this.toDecodeString = toDecodeString;

        //loads Engine from xml file.
        //Ofek - please fix it Ahi - ci nishbar li hazain. :-)
        //The file looks bad, open it and look - file path inside the method loadEngimaFromFile.
        try {
            engine = loadEnigmaFromFile();
        } catch (invalidInputException e) {
            throw new RuntimeException(e);
        }

    }


    private static Engine loadEnigmaFromFile() throws invalidInputException {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:\\Work\\Java\\Enigma\\Engine\\src\\resources\\EngineLoader.xml"))) {
            Engine engine = (Engine) in.readObject();
            return engine;
        } catch (IOException | ClassNotFoundException ex) {
            throw new invalidInputException("Error in loading a file\n");
        }

    }

    /**In here the Agent should try to decode the String that the ThreadPool gave him.
     * Pay attention!
     * if mission's size is 4, then the Agent should do the WHOLE options.
     * for example, if the Rotors first positions is A,A,A ,
     * then the agent should do A,A,E , A,A,F, A,A,G , A,A,H - so each time he will start from different configuration,
     * in order to cover all possible cases.
     *
     */
    @Override
    public void run(){

    }

    /*public static void main(String[] args) {
        Agent agent = new Agent(1, "ABC");
    }*/
}
