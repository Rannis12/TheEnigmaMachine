package application;

import dtos.*;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;

import java.io.File;
import java.util.ArrayList;

public class MainPageController {
    @FXML private FirstTabController firstTabController;
    @FXML private SecondTabController secondTabController;

    @FXML private ThirdTabController thirdTabController;
    private Engine engine;

    private int currConfigurationDecodedAmount;

    @FXML public void initialize() {
        resetCurrConfigurationDecodedAmount();

        if(firstTabController != null && secondTabController != null && thirdTabController != null){
            firstTabController.setMainController(this);
            secondTabController.setMainPageController(this);
            thirdTabController.setMainPageController(this);


            // Ran: Bind number of decodes to counter.
            firstTabController.getDecodedStrings().textProperty().bind(secondTabController.getAmountOfDecodedStrings().asObject().asString());

            //Ran: everytime that amountOfDecodedStrings is changes, we "listening" to event, and updates the relevant fields.
            // in this case, we update configuration label.
            secondTabController.getAmountOfDecodedStrings().addListener(e -> {
            updateConfigurationLabel();
            });


        }

    }
    public void updateConfigurationLabel() {
        EngineFullDetailsDTO engineFullDetailsDTO = getEngineFullDetails();
        String newConfiguration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());

        firstTabController.setCurrentConfigurationTF(newConfiguration);
        secondTabController.setCurrentConfigurationTF(newConfiguration);
        thirdTabController.setCurrentConfigurationTF(newConfiguration);
    }
    @FXML private Button loadFileBtn;
    @FXML private Label xmlPathLabel;
    @FXML private TabPane tabPane;
    @FXML private Tab tabOne;
    @FXML private Tab tabTwo;
    @FXML private Tab tabThree;



    @FXML
    void loadFile(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            if(loadFileFromXml(file.getAbsolutePath())) {
                firstTabController.showDetails(engine.getEngineMinimalDetails());
                firstTabController.enableButtons();
                firstTabController.clearConfigurationTextFields();


                secondTabController.disableAllButtonsAndTextFields();
                secondTabController.clearCurrentConfigurationTA();
                secondTabController.getStatisticsTA().clear();
            }
        }
    }


    public boolean loadFileFromXml(String fileDestination){

        try {

           EngineLoader engineLoader = new EngineLoader(fileDestination);
           engine = engineLoader.loadEngineFromXml(fileDestination);

          firstTabController.setReflectorsCB(getReflectorsIDs());
          //firstTabController.setReflectorsMenuBtn(getReflectorsIDs());

            engine.resetStatistics();

          xmlPathLabel.setText(fileDestination + " selected");
          secondTabController.setDecodingButtonsDisable(true);

          return true;

        } catch (invalidXMLfileException e) {
            popUpError(e.getMessage());
        }
        return false;
    }

    public void randomConfiguration() {
        engine.randomEngine();
    }

    public EngineFullDetailsDTO getEngineFullDetails(){
        return engine.getEngineFullDetails();
    }

    public String makeCodeForm(ArrayList<Integer> notchesPlaces, ArrayList<Integer> RotorsOrganization,
                                String rotorsPositions, String chosenReflector, String plugBoardString) {
        String finalInfoToPrint = "<";

        for (int i = 0; i < RotorsOrganization.size(); i++) {
            if (i + 1 != RotorsOrganization.size())
                finalInfoToPrint += (RotorsOrganization.get(i) + ",");
            else
                finalInfoToPrint += RotorsOrganization.get(i);
        }
        finalInfoToPrint = finalInfoToPrint + "><";
        for (int i = 0; i < rotorsPositions.length(); i++) {
            if (i + 1 != notchesPlaces.size())
                finalInfoToPrint = finalInfoToPrint + rotorsPositions.charAt(i) + "(" + notchesPlaces.get(i) + "),";
            else
                finalInfoToPrint = finalInfoToPrint + rotorsPositions.charAt(i) + "(" + notchesPlaces.get(i) + ")>";
        }
        finalInfoToPrint = finalInfoToPrint +"<" + chosenReflector + ">";

        if (!plugBoardString.equals("")) {
            finalInfoToPrint = finalInfoToPrint + "<" + plugBoardString + ">";
        }
        return finalInfoToPrint;
    }


    public void setTabsConfiguration(String newConfiguration) {
        firstTabController.setCurrentConfigurationTF(newConfiguration);
        secondTabController.setCurrentConfigurationTF(newConfiguration);
        thirdTabController.setCurrentConfigurationTF(newConfiguration);

        secondTabController.getStatisticsTA().appendText(newConfiguration + '\n');
    }

    public void setDecodingAndClearButtonsDisable(boolean setToDisable) {
        secondTabController.setDecodingButtonsDisable(setToDisable);
    }

    public int getUsedAmountOfRotors() {
        return engine.getEngineMinimalDetails().getUsedAmountOfRotors();
    }

    public void checkRotorIndexesValidity(String rotorsPosition, RotorsIndexesDTO rotorsIndexesDTO) throws invalidInputException {
        engine.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO.getUIRotorsIndexes());
    }

    public void checkRotorsFirstPositionsValidity(String rotorsFirstPositions) throws invalidInputException {
        engine.checkRotorsFirstPositionsValidity(rotorsFirstPositions, engine.getKeyBoard());
    }

    public void checkPlugBoardValidity(String tmpString, PlugBoardDTO plugBoardDTO) throws invalidInputException {
        engine.checkPlugBoardValidity(tmpString, plugBoardDTO.getUICables());
    }

    public void setNewMachine(RotorsFirstPositionDTO rotorsFirstPositionDTO, PlugBoardDTO plugBoardDTO, ReflectorDTO reflectorDTO, RotorsIndexesDTO rotorsIndexesDTO) {
        engine.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);
    }

    public int getReflectorsIDs(){
        return engine.getReflectorsIDs();
    }
    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }


    public DecodeStringInfo decodeString(String text) throws invalidInputException {

        DecodeStringInfo decodeStringInfo = engine.decodeStr(text);
        return decodeStringInfo;
    }

    public char decodeChar(char character){
        return engine.decodeChar(character);
    }

    public void increaseDecodedStringAmount(){
        currConfigurationDecodedAmount++;
    }
    public int getCurrConfigurationDecodedAmount() {
        return currConfigurationDecodedAmount;
    }
    public void resetCurrConfigurationDecodedAmount() {
        currConfigurationDecodedAmount = 0;
    }

    public Engine getEngine() {
        return engine;
    }

    public void resetEngineToUserInitChoice() {
        engine.resetEngineToUserInitChoice();
    }
}

