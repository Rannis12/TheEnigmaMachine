package ui;

import dtos.*;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static logic.enigma.EngineLoader.isFileExistAndXML;

public class Utils {
    public void run() {
        boolean shouldContinue = true;
        Engine engine = null;
        int userChoice;
        System.out.println("Welcome!");
        System.out.println("Today we're going to build an enigma machine.\n");

        Scanner scanner = new Scanner(System.in);
        boolean isExistEngine = false, option3Occurred = false, option4Occurred = false;

        while (shouldContinue) {
            showMenu();
            try {
                userChoice = Integer.parseInt(scanner.nextLine());
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("invalid input! please choose number between 1 to 10\n");
                continue;
            }

            switch (userChoice) {
                case 1:
                    try {
                        engine = optionOne();
                        isExistEngine = true;
                        option3Occurred = false;
                        option4Occurred = false;
                    } catch (invalidXMLfileException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 2:
                    if (isExistEngine) {
                        optionTwo(engine, option3Occurred || option4Occurred);
                    } else {
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    }
                    break;
                case 3:
                    if (isExistEngine) {
                        option3Occurred = optionThree(engine);
                    } else {
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    }
                    break;
                case 4:
                    if (isExistEngine) {
                        optionFour(engine);
                        option4Occurred = true;
                    } else {
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.");
                    }
                    break;
                case 5:
                    if (isExistEngine) {
                        if (option3Occurred || option4Occurred) {
                            optionFive(engine, scanner);
                        } else {
                            System.out.println("Error: didnt load machine details. Please choose option 3 or option 4 first.\n");
                        }
                    } else {
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    }
                    break;
                case 6:
                    if (isExistEngine) {
                        if (option3Occurred || option4Occurred) {
                            optionSix(engine);
                        } else
                            System.out.println("Error: Enigma machine hasn't been set yet!\n");
                    } else
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    break;
                case 7:
                    if (isExistEngine) {
                        if (option3Occurred || option4Occurred) {
                            optionSeven(engine);
                        } else
                            System.out.println("Error: Enigma machine hasn't been set!\n");
                    } else
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    break;
                case 8:
                    shouldContinue = false;
                    System.out.println("Hope you enjoyed! see you next time :)");
                    break;

                case 9:
                    if(isExistEngine) {
                        if(option3Occurred || option4Occurred) {
                            try {
                                saveEnigmaToFile(engine);
                            } catch (invalidInputException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        else {
                            System.out.println("Error: didnt load machine details. Please choose option 3 or option 4 first.\n");
                        }
                    }
                    else {
                        System.out.println("The machine hasn't built yet. Please do Option 1 before you get here.\n");
                    }
                    break;
                case 10:
                    try {
                        engine = loadEnigmaFromFile();
                        isExistEngine = true;
                        option3Occurred = true;
                    } catch (invalidInputException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid input! please enter a number between 1 to 10:\n");
            }
        }
    }


    public Engine optionOne() throws invalidXMLfileException {
        Engine engine = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hey, please insert a full path to your requested xml file: ");
        String fileDestination = scanner.nextLine();

        if (isFileExistAndXML(fileDestination)) {
            EngineLoader engineLoader = new EngineLoader(fileDestination);
            engine = engineLoader.loadEngineFromXml(fileDestination);

            engine.resetStatistics();

            System.out.println("File loaded successfully!\n");
        }
        return engine;
    }
    private void optionTwo(Engine engine, boolean isFullEngine) {
        if (isFullEngine)
            showFullDetails(engine);
        else
            showMinimalDetails(engine);
    }
    private boolean optionThree(Engine engine) {
        Scanner scanner = new Scanner(System.in);
        boolean isValidRotors = false, isValidFirstPositions = false, isValidReflector = false, isValidPlugBoards = false;
        boolean userWantToEscape = false, isMachineSet = false;

        RotorsIndexesDTO rotorsIndexesDTO = new RotorsIndexesDTO();
        PlugBoardDTO plugBoardDTO = new PlugBoardDTO();

        RotorsFirstPositionDTO rotorsFirstPositionDTO = null;
        ReflectorDTO reflectorDTO = null;


        while (!userWantToEscape && !isMachineSet) {


            while (!isValidRotors) {
                System.out.println("Please enter " + engine.getEngineMinimalDetails().getUsedAmountOfRotors() + " rotors ID's." +
                        "\nInsert the most left rotor at first, and the Rightest rotor at the end - " +
                        "divided by comma. for example: 23,542,231,545.");
                String rotorsPosition = scanner.nextLine();
                try {
                    engine.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO.getUIRotorsIndexes());
                    isValidRotors = true;
                } catch (invalidInputException ex) {
                    System.out.println(ex.getMessage());
                    if (showMiniMenu(scanner) == 2) {
                        isValidRotors = true;
                        userWantToEscape = true;
                    }
                    //else - i=1 so continue
                }
            }
            if (userWantToEscape)
                continue;

            while (!isValidFirstPositions) {
                System.out.println("Please enter first positions of rotors from left to right in a row - " +
                        "no spaces needed. for example: 4D8A.");
                String rotorsFirstPositions = scanner.nextLine();
                rotorsFirstPositions = rotorsFirstPositions.toUpperCase();
                try {
                    engine.checkRotorsFirstPositionsValidity(rotorsFirstPositions, engine.getKeyBoard());
                    rotorsFirstPositionDTO = new RotorsFirstPositionDTO(rotorsFirstPositions);
                    isValidFirstPositions = true;
                } catch (invalidInputException ex) {
                    System.out.println(ex.getMessage());
                    if (showMiniMenu(scanner) == 2) {
                        isValidFirstPositions = true;
                        userWantToEscape = true;
                    }
                    //else - i=1 so continue
                }
            }
            if (userWantToEscape)
                continue;

            while (!isValidReflector) {
                System.out.println("Please enter reflector number between 1 to 5:");
                try {
                    String tmpString = scanner.nextLine();
                    engine.checkSelectedReflectorValidity(tmpString);
                    reflectorDTO = new ReflectorDTO(Integer.parseInt(tmpString));
                    isValidReflector = true;
                } catch (invalidInputException ex) {
                    System.out.println(ex.getMessage());
                    if (showMiniMenu(scanner) == 2) {
                        isValidReflector = true;
                        userWantToEscape = true;
                    }
                    //else - i=1 so continue
                }
            }
            if (userWantToEscape)
                continue;

            while (!isValidPlugBoards) {
                System.out.println("Please enter contiguous string of characters that forming pairs in plugboard: for example [DK49 !]");
                try {
                    String tmpString = scanner.nextLine();
                    tmpString = tmpString.toUpperCase();
                    engine.checkPlugBoardValidity(tmpString, plugBoardDTO.getUICables());
                    plugBoardDTO.setInitString(tmpString);
                    isValidPlugBoards = true;
                } catch (invalidInputException ex) {
                    System.out.println(ex.getMessage());
                    if (showMiniMenu(scanner) == 2) {
                        isValidPlugBoards = true;
                        userWantToEscape = true;
                    }
                    //else - i=1 so continue
                }
            }
            if (userWantToEscape)
                continue;

            engine.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);

            EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
            String currConfiguration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                    engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());

            ConfigurationStatisticsDTO configurationStatistics = new ConfigurationStatisticsDTO(currConfiguration);
            engine.getMachineStatistics().addConfiguration(configurationStatistics);
            System.out.println("Machine initialized successfully!\n");
            isMachineSet = true;
        }
        return isMachineSet;
    }
    private void optionFour(Engine engine) {
        engine.randomEngine();

        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
        String currConfiguration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());


        ConfigurationStatisticsDTO configurationStatistics = new ConfigurationStatisticsDTO(currConfiguration);
        engine.getMachineStatistics().addConfiguration(configurationStatistics);

        System.out.println("Random Engine made successfully!");
        System.out.println("Engine configurations are: " + currConfiguration + "\n");

    }
    private void optionFive(Engine engine, Scanner scanner) {//TO DO

        System.out.println("Please enter a Sentence/Word you would like to decode: ");
        String stringToDecode = scanner.nextLine();

        try {
            DecodeStringInfo newInfo = engine.decodeStr(stringToDecode);
            System.out.println("String " + newInfo.getToEncodeString() + " decoded to: " + newInfo.getDecodedString() + "\n");

            engine.getMachineStatistics().addDecodedStringInfo(newInfo);

        } catch (invalidInputException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void optionSix(Engine engine) {
        engine.resetEngineToUserInitChoice();
        System.out.println("Reset has been done successfully!\n");
    }
    private void optionSeven(Engine engine) {
        ArrayList<ConfigurationStatisticsDTO> statistics = engine.getMachineStatistics().getStatisticsArrayList();
        for (ConfigurationStatisticsDTO statistic : statistics) {
            System.out.println("" + statistic.getConfigurationStr());
            ArrayList<DecodeStringInfo> tmpConfigurationArr = statistic.getInfoArray();
            if (tmpConfigurationArr.size() == 0) {
                System.out.println("-----");
            }
            for (int j = 0; j < tmpConfigurationArr.size(); j++) {
                System.out.println((j + 1) + ". <" + tmpConfigurationArr.get(j).getToEncodeString() +
                        "> --> <" + tmpConfigurationArr.get(j).getDecodedString() + "> (" + tmpConfigurationArr.get(j).getTimeInMilli() + " nano seconds)");
            }
        }
        System.out.println("");
    }
    private void saveEnigmaToFile(Engine engine) throws invalidInputException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose Full path to save the file to:");
        String path = scanner.nextLine();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(engine);
            out.flush();
            System.out.println("File saved successfully!\n");

        } catch (IOException e) {
            throw new invalidInputException("Error: could not save file. Please try again.\n");
        }
    }
    private Engine loadEnigmaFromFile() throws invalidInputException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose Full path to load a file from:");
        String path = scanner.nextLine();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            Engine engine = (Engine) in.readObject();
            System.out.println("Enigma machine loaded successfully from file!\n");
            return engine;
        } catch (IOException | ClassNotFoundException ex) {
            throw new invalidInputException("Error in loading a file\n");
        }

    }
    private String makeCodeForm(ArrayList<Integer> notchesPlaces, ArrayList<Integer> RotorsOrganization,
                                String rotorsPositions, String chosenReflector, String plugBoardString) {
        String finalInfoToPrint = "<";

        for (int i = 0; i < notchesPlaces.size(); i++) {
            if (i + 1 != notchesPlaces.size())
                finalInfoToPrint += (RotorsOrganization.get(i) + "(" + notchesPlaces.get(i) + "),");
            else
                finalInfoToPrint += (RotorsOrganization.get(i) + "(" + notchesPlaces.get(i) + ")");
        }
        finalInfoToPrint = finalInfoToPrint + "><" + rotorsPositions + "><" + chosenReflector + ">";
        if (!plugBoardString.equals("")) {
            finalInfoToPrint = finalInfoToPrint + "<" + plugBoardString + ">";
        }
        return finalInfoToPrint;
    }

    public void showMenu() {
        System.out.println("Please choose from the following: ");
        System.out.println("1:  Load Enigma machine from XML file");
        System.out.println("2:  Print machine Details");
        System.out.println("3:  Set definitions of Enigma machine");
        System.out.println("4:  Generate auto Enigma machine");
        System.out.println("5:  Decode line");
        System.out.println("6:  Reset machine to last definitions");
        System.out.println("7:  Show history and Statistics");
        System.out.println("8:  Exit.");
        System.out.println("9.  Save machine to file (--BONUS--)");
        System.out.println("10. Load machine from file (--BONUS--)");
    }

    private int showMiniMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("choose from the following:\n1- insert correct input\n2- Go back to main menu");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                choice = -1;
            }
        } while (choice != 1 && choice != 2);
        return choice;
    }

    private void showMinimalDetails(Engine engine) {
        EngineMinimalDetailsDTO engineMinimalDetailsDTO = engine.getEngineMinimalDetails();
        System.out.println("Num of used rotors/possible rotors : " + engineMinimalDetailsDTO.getUsedAmountOfRotors() + "/" + engineMinimalDetailsDTO.getRotorsAmount());
        System.out.println("Reflectors amount: " + engineMinimalDetailsDTO.getReflectorsAmount());
        System.out.println("Decoded messages so far: 0\n");

    }

    private void showFullDetails(Engine engine) {
        EngineFullDetailsDTO engineFullDetailsDTO = engine.getEngineFullDetails();
        System.out.println("Numbers of used rotors/possible rotors : " + engineFullDetailsDTO.getUsedAmountOfRotors() + "/" + engineFullDetailsDTO.getRotorsAmount());
        System.out.println("Reflectors amount: " + engineFullDetailsDTO.getReflectorsAmount());
        System.out.println("Decoded messages so far: " + engineFullDetailsDTO.getAmountOfDecodedStrings());

        System.out.println("\nOriginal setting Enigma machine:");
        String originalSettings = makeCodeForm(engineFullDetailsDTO.getNotchesFirstPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsFirstPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());
        System.out.println(originalSettings);

        System.out.println("\nCurrent setting Enigma machine:");
        String currentSettings = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());
        System.out.println(currentSettings + "\n");
    }

}
