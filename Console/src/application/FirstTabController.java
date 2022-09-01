package application;

import dtos.*;
import exceptions.invalidInputException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;


public class FirstTabController {
        private RotorsIndexesDTO rotorsIndexesDTO = null;
        private PlugBoardDTO plugBoardDTO = null;
        private RotorsFirstPositionDTO rotorsFirstPositionDTO = null;
        private ReflectorDTO reflectorDTO = null;
        private MainPageController mainPageController;
        private int switchOption = 1;


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
        private GridPane rowTwoInTabOne;

        @FXML
        private Label decodedStrings;

        @FXML
        private Label decodedStringsLabel;

        @FXML
        private GridPane rowThreeInTabOne;

        @FXML
        private Label reflectorsAmount;

        @FXML
        private Label reflectorsAmountLabel;

        @FXML
        private SplitPane splitPane;

        @FXML
        private AnchorPane APLeftInSP;

        @FXML
        private VBox VBInLeftSP;

        @FXML
        private Label calibrationLabel;

        @FXML
        private HBox HBInLeftSP;

        @FXML
        private Button randomBtn;

        @FXML
        private Button manualBtn;

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
        private AnchorPane APRightInSP;

        @FXML
        private VBox VBInRightSP;

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
    void TODOinCalibration(ActionEvent event) { /*!!!!!!!!!!!!!!!!*/

    }

    @FXML
    void actionOnManualBtn(ActionEvent event) {
        userRotorsInput.setDisable(false);

        userInitPlaces.setDisable(true);
        userInitPlugBoard.setDisable(true);
        reflectorCB.setDisable(true);
    }

    @FXML
    void randomBtnListener(ActionEvent event) {
        mainPageController.randomConfiguration();

        operationsAfterValidInput();

        mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());

        mainPageController.enableDecodingAndClearButtons();

    }

    @FXML
    void reflectorCBListener(ActionEvent event) {
        int chosenReflector = (Integer)reflectorCB.getValue() - 1;
        reflectorDTO = new ReflectorDTO(chosenReflector);
        reflectorCB.setDisable(true);
        mainPageController.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);

        operationsAfterValidInput();

        mainPageController.setTabsConfiguration(initializeConfigurationTF.getText());

    }

    @FXML
    void userInitPlacesListener(ActionEvent event) {
        String rotorsFirstPositions = userInitPlaces.getText();
        rotorsFirstPositions = rotorsFirstPositions.toUpperCase();
        try {
            //engine.checkRotorsFirstPositionsValidity(rotorsFirstPositions, engine.getKeyBoard());
            mainPageController.checkRotorsFirstPositionsValidity(rotorsFirstPositions);
            rotorsFirstPositionDTO = new RotorsFirstPositionDTO(rotorsFirstPositions);
            userInitPlaces.setDisable(true);
            userInitPlugBoard.setDisable(false);
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
            //engine.checkPlugBoardValidity(tmpString, plugBoardDTO.getUICables());
            plugBoardDTO.setInitString(tmpString);
            userInitPlugBoard.setDisable(true);
            reflectorCB.setDisable(false);
        } catch (invalidInputException ex) {
            userInitPlugBoard.clear();
            System.out.println(ex.getMessage());
        }
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
        } catch (invalidInputException ex) {
            userRotorsInput.clear();
            mainPageController.popUpError(ex.getMessage());
        }

    }


    public void setCurrentConfigurationLabel(String currentConfiguration) {
        currentConfigurationTF.setText(currentConfiguration);
    }


    public void showDetails(EngineMinimalDetailsDTO engineMinimalDetailsDTO) {

        rotorsDetails.setText("used/possible : " + engineMinimalDetailsDTO.getUsedAmountOfRotors() + "/" + engineMinimalDetailsDTO.getRotorsAmount());
        reflectorsAmount.setText(String.valueOf(engineMinimalDetailsDTO.getReflectorsAmount()));
        decodedStrings.setText(String.valueOf(engineMinimalDetailsDTO.getAmountOfDecodedStrings()));
    }

 /*   @FXML
    void onEnter(ActionEvent event) { //when the user enters 'enter', we can get the input using the method below.

        switch(switchOption){
            case 1:
                String instruction = "Please enter " + mainPageController.getUsedAmountOfRotors() + " rotors ID's." +
                        "\nInsert the most left rotor at first, and the Rightest rotor at the end - " +
                        "divided by comma. for example: 23,542,231,545.";
                instructionToUserTF.setText(instruction);

                String rotorsPosition = userInputInCalibration.getText();
                try {
                    rotorsIndexesDTO = new RotorsIndexesDTO();
                    mainPageController.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO);
                    //isValidRotors = true;
                } catch (invalidInputException ex) {
                    // System.out.println(ex.getMessage()); ///open error stage instead.
                }

                    case 2:

            case 3:

            case 4:



        }
}*/

    public void setReflectorsCB(ArrayList<Integer> reflectorsIDs){
            ObservableList observableList = FXCollections.observableArrayList(reflectorsIDs);
            reflectorCB.setItems(observableList);
            //tilePane.getChildren().add(reflectorCB);
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
    }
    public void clearAllUsersTextFields(){
        userInitPlaces.clear();
        userRotorsInput.clear();
        userInitPlugBoard.clear();
        //reflectorCB.getSelectionModel().clearSelection();
    }

}