package application;

import dtos.*;
import exceptions.invalidInputException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FirstTabController implements Initializable {
        private RotorsIndexesDTO rotorsIndexesDTO = null;
        private PlugBoardDTO plugBoardDTO = null;
        private RotorsFirstPositionDTO rotorsFirstPositionDTO = null;
        private ReflectorDTO reflectorDTO = null;
        private MainPageController mainPageController;

    @FXML
    private AnchorPane innerTabOneAP;

    @FXML
    private VBox tabOneUpperVB;

    @FXML
    private Label detailsLabel;

    @FXML
    private GridPane rowOneInTabOne;

    @FXML
    private Label rotorsDetails;

    @FXML
    private Label rotorsLabel;

    @FXML
    private Label decodedStrings;

    @FXML
    private Label decodedStringsLabel;

    @FXML
    private Label reflectorsAmount;

    @FXML private ToggleButton setMachineBtn;


    @FXML
    private Label reflectorsAmountLabel;

    @FXML
    private Label calibrationLabel;

    @FXML
    private Button randomBtn;

    @FXML
    private Button manualBtn;

    @FXML
    private TextArea instructionTF;

    @FXML
    private TextField userRotorsInput;

    @FXML
    private TextField userInitPlaces;

    @FXML
    private TextField userInitPlugBoard;

    @FXML
    private ComboBox<?> reflectorCB;

    @FXML
    private Label machineInitializeLabel;

    @FXML
    private Label currentMachineLabel;

    @FXML
    private Label initializedConfigurationLabel;

    @FXML
    private TextField initializeConfigurationTF;

    @FXML
    private Label currentConfigurationLabel;

    @FXML
    private TextField currentConfigurationTF;


    @FXML
    void setMachine(ActionEvent event) {
        //reflectorCB.getItems().removeAll(reflectorCB.getItems());//////////// to fucking check!!!
        setReflectorsCB(mainPageController.getReflectorsIDs());
        setMachineBtn.setSelected(false);

        mainPageController.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);
        operationsAfterValidInput();
        mainPageController.resetCurrConfigurationDecodedAmount();//reset the amount of strings decoded with current configuration

        mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());

    }

    @FXML
    void randomBtnListener(ActionEvent event) {
        mainPageController.randomConfiguration();
        mainPageController.randomConfiguration();

        operationsAfterValidInput();
        mainPageController.resetCurrConfigurationDecodedAmount();
        mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());
    }

    @FXML
    void setReflectorID(ActionEvent event) {
        int chosenReflector = (Integer) reflectorCB.getValue();

        reflectorDTO = new ReflectorDTO(chosenReflector);
        reflectorCB.setDisable(true);
        setMachineBtn.setDisable(false);
    }


    public void setCurrentConfigurationLabel(String currentConfiguration) {
        currentConfigurationTF.setText(currentConfiguration);
    }

    public Label getDecodedStrings() {
            return this.decodedStrings;
    }


    public void showDetails(EngineMinimalDetailsDTO engineMinimalDetailsDTO) {

        rotorsDetails.setText("used/possible : " + engineMinimalDetailsDTO.getUsedAmountOfRotors() + "/" + engineMinimalDetailsDTO.getRotorsAmount());
        reflectorsAmount.setText(String.valueOf(engineMinimalDetailsDTO.getReflectorsAmount()));
        //decodedStrings.setText(String.valueOf(engineMinimalDetailsDTO.getAmountOfDecodedStrings()));

//        IntegerProperty counter = mainPageController.getDecodedStringsCounterDTO();

    }

    public void setMainController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void enableButtons() {
        randomBtn.setDisable(false);
        manualBtn.setDisable(false);
    }


    public void operationsAfterValidInput(){
        clearAllUsersTextFields();
        machineInitializeLabel.setTextFill(Color.RED);


        EngineFullDetailsDTO engineFullDetailsDTO = mainPageController.getEngineFullDetails();
        String newConfiguration = mainPageController.makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());

        initializeConfigurationTF.setText(newConfiguration);
        mainPageController.setDecodingAndClearButtonsDisable(false);
    }
    public void clearAllUsersTextFields(){
        userInitPlaces.clear();
        userRotorsInput.clear();
        userInitPlugBoard.clear();
        instructionTF.clear();
    }

    @FXML
    void manualBtnListener(ActionEvent event) {
        userRotorsInput.setDisable(false);
        clearAllUsersTextFields();
        instructionTF.setPromptText("enter " + mainPageController.getUsedAmountOfRotors() + " rotors ID's " +
                "from left to right divided by comma. for example 23,542,231");


        userInitPlaces.setDisable(true);
        userInitPlugBoard.setDisable(true);
        reflectorCB.setDisable(true);

        machineInitializeLabel.setTextFill(Color.valueOf("faf2f2"));
    }
    @FXML
    void userRotorsInputListener(ActionEvent event) {


        String rotorsPosition = userRotorsInput.getText();
        System.out.println(userRotorsInput.getText());
        try {
            rotorsIndexesDTO = new RotorsIndexesDTO();
            mainPageController.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO);
            userRotorsInput.setDisable(true);

            userInitPlaces.setDisable(false);
            instructionTF.setPromptText("Enter first positions of rotors from left to right without spaces. For example: 4D8A.");

        } catch (invalidInputException ex) {
            userRotorsInput.clear();
            mainPageController.popUpError(ex.getMessage());
        }

    }

    @FXML
    void userInitPlacesListener(ActionEvent event) {
        String rotorsFirstPositions = userInitPlaces.getText();
        rotorsFirstPositions = rotorsFirstPositions.toUpperCase();
        try {
            mainPageController.checkRotorsFirstPositionsValidity(rotorsFirstPositions);
            rotorsFirstPositionDTO = new RotorsFirstPositionDTO(rotorsFirstPositions);
            userInitPlaces.setDisable(true);

            userInitPlugBoard.setDisable(false);

            instructionTF.setPromptText("Enter contiguous string of characters that forming pairs in plugboard. For example [DK49 !]");
        } catch (invalidInputException ex) {
            userInitPlaces.clear();
            mainPageController.popUpError(ex.getMessage());
        }
    }

    @FXML
    void userInitPlugBoardListener(ActionEvent event) {
        try {
            String tmpString = userInitPlugBoard.getText();
            tmpString = tmpString.toUpperCase();
            plugBoardDTO = new PlugBoardDTO();
            mainPageController.checkPlugBoardValidity(tmpString, plugBoardDTO);
            plugBoardDTO.setInitString(tmpString);
            userInitPlugBoard.setDisable(true);

            reflectorCB.setDisable(false);
            instructionTF.setPromptText("Choose reflectors from the following:");
        } catch (invalidInputException ex) {
            userInitPlugBoard.clear();
            mainPageController.popUpError(ex.getMessage());
        }
    }


    public void setReflectorsCB(ArrayList<Integer> reflectorsIDs){

        ObservableList observableList = FXCollections.observableArrayList(reflectorsIDs);

        reflectorCB.getItems().removeAll(reflectorCB.getItems());//ofek - exception

        reflectorCB.setItems(observableList);
    }

    public void reLoadInitialize() {
        rotorsDetails.setText("...");
        //decodedStrings.setText("...");
        reflectorsAmount.setText("...");

        initializeConfigurationTF.clear();
        currentConfigurationTF.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}