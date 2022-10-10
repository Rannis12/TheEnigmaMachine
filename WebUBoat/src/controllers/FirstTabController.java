package controllers;

import dtos.*;
import exceptions.invalidInputException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


public class FirstTabController implements Initializable {
    private RotorsIndexesDTO rotorsIndexesDTO = null;
    private PlugBoardDTO plugBoardDTO = null;
    private RotorsFirstPositionDTO rotorsFirstPositionDTO = null;
    private ReflectorDTO reflectorDTO = null;
    private UBoatController UBoatController;

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
    private Label chooseReflectorLabel;

    @FXML
    private ChoiceBox<?> reflectorChoiceBox;

    @FXML
    private Button setMachineBtn;

    @FXML
    private Label machineInitializeLabel;

    @FXML
    private TextField currentConfigurationTF;

    @FXML
    private Label detailsLabel;

    @FXML
    private Label rotorsDetails;

    @FXML
    private Label reflectorsAmount;


    @FXML
    void setMachine(ActionEvent event) throws invalidInputException {
        try {
            int chosenReflector = (Integer) reflectorChoiceBox.getValue();
            reflectorDTO = new ReflectorDTO(chosenReflector);

            UBoatController.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);
            operationsAfterValidInput();
            UBoatController.resetCurrConfigurationDecodedAmount();//reset the amount of strings decoded with current configuration
//            mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());

            chooseReflectorLabel.setVisible(false);
            reflectorChoiceBox.setVisible(false);
            setMachineBtn.setDisable(true);
        }
        catch (RuntimeException ex) {
            UBoatController.popUpError("Please choose reflector before setting a new machine!");
        }


    }
    @FXML
    void randomBtnListener(ActionEvent event) {
        UBoatController.randomConfiguration();

        operationsAfterValidInput();
        UBoatController.resetCurrConfigurationDecodedAmount();
//        mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());
    }
    @FXML
    void manualBtnListener(ActionEvent event) {
        userRotorsInput.setDisable(false);
        clearAllUsersTextFields();
        instructionTF.setText("enter " + UBoatController.getUsedAmountOfRotors() + " rotors ID's " +
                "from left to right divided by comma. for example 23,542,231");


        userInitPlaces.setDisable(true);
        userInitPlugBoard.setDisable(true);

        machineInitializeLabel.setTextFill(Color.valueOf("faf2f2"));
    }
    @FXML
    void userRotorsInputListener(ActionEvent event) {
        String rotorsPosition = userRotorsInput.getText();
        try {
            rotorsIndexesDTO = new RotorsIndexesDTO();
            UBoatController.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO);
            userRotorsInput.setDisable(true);

            userInitPlaces.setDisable(false);
            instructionTF.setText("Enter first positions of rotors from left to right without spaces. For example: 4D8A.");

        } catch (invalidInputException ex) {
            userRotorsInput.clear();
            UBoatController.popUpError(ex.getMessage());
        }

    }
    @FXML
    void userInitPlacesListener(ActionEvent event) {
        String rotorsFirstPositions = userInitPlaces.getText();
        rotorsFirstPositions = rotorsFirstPositions.toUpperCase();
        try {
            UBoatController.checkRotorsFirstPositionsValidity(rotorsFirstPositions);
            rotorsFirstPositionDTO = new RotorsFirstPositionDTO(rotorsFirstPositions);
            userInitPlaces.setDisable(true);

            userInitPlugBoard.setDisable(false);

            instructionTF.setText("Enter contiguous string of characters that forming pairs in plugboard. For example [DK49 !]");
        } catch (invalidInputException ex) {
            userInitPlaces.clear();
            UBoatController.popUpError(ex.getMessage());
        }
    }
    @FXML
    void userInitPlugBoardListener(ActionEvent event)


    {
        try {
            String tmpString = userInitPlugBoard.getText();
            tmpString = tmpString.toUpperCase();
            plugBoardDTO = new PlugBoardDTO();
            UBoatController.checkPlugBoardValidity(tmpString, plugBoardDTO);
            plugBoardDTO.setInitString(tmpString);
            userInitPlugBoard.setDisable(true);

            chooseReflectorLabel.setVisible(true);
            reflectorChoiceBox.setVisible(true);
            setMachineBtn.setDisable(false);

            instructionTF.setText("Choose reflectors from the following:");
        } catch (invalidInputException ex) {
            userInitPlugBoard.clear();
            UBoatController.popUpError(ex.getMessage());
        }
    }


    public void setReflectorsCB(int amount){
        ObservableList observableList = FXCollections.observableArrayList();
        for (int i = 0; i < amount; i++) {
            observableList.add(i+1);
        }
        reflectorChoiceBox.getItems().clear();
        reflectorChoiceBox.setItems(observableList);
    }


    public void setCurrentConfigurationTF(String currentConfiguration) {
        currentConfigurationTF.setText(currentConfiguration);
    }

    /*public Label getDecodedStrings() {
        return this.decodedStrings;
    }*/

    public void showDetails(EngineMinimalDetailsDTO engineMinimalDetailsDTO) {

        rotorsDetails.setText("used/possible : " + engineMinimalDetailsDTO.getUsedAmountOfRotors() + "/" + engineMinimalDetailsDTO.getRotorsAmount());
        reflectorsAmount.setText(String.valueOf(engineMinimalDetailsDTO.getReflectorsAmount()));
        //decodedStrings.setText(String.valueOf(engineMinimalDetailsDTO.getAmountOfDecodedStrings())); // solved with bind

    }
    public void setMainController(UBoatController UBoatController) {
        this.UBoatController = UBoatController;
    }
    public void enableButtons() {
        randomBtn.setDisable(false);
        manualBtn.setDisable(false);
    }
    public void operationsAfterValidInput(){
        clearAllUsersTextFields();
        machineInitializeLabel.setTextFill(Color.RED);


        EngineFullDetailsDTO engineFullDetailsDTO = UBoatController.getEngineFullDetails();
        String newConfiguration = UBoatController.makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString());

//        initializeConfigurationTF.setText(newConfiguration);
        UBoatController.setDecodingAndClearButtonsDisable(false);

    }
    public void clearAllUsersTextFields(){
        userInitPlaces.clear();
        userRotorsInput.clear();
        userInitPlugBoard.clear();
        instructionTF.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void clearConfigurationTextFields() {
//        initializeConfigurationTF.clear();
        currentConfigurationTF.clear();
    }
}