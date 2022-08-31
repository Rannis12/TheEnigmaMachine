package application;

import dtos.EngineFullDetailsDTO;
import dtos.EngineMinimalDetailsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FirstTabController {

    private MainPageController mainPageController;

    @FXML private AnchorPane innerTabOneAP;

    @FXML private VBox tabOneUpperVB;

    @FXML private Label detailsLabel;

    @FXML private GridPane rowOneInTabOne;
    @FXML private Label rotorsDetails;

    @FXML private Label rotorsLabel;

    @FXML private GridPane rowTwoInTabOne;

    @FXML private Label decodedStrings;

    @FXML private Label decodedStringsLabel;

    @FXML private GridPane rowThreeInTabOne;

    @FXML private Label reflectorsAmount;

    @FXML private Label reflectorsAmountLabel;

    @FXML private SplitPane splitPane;

    @FXML private AnchorPane APLeftInSP;

    @FXML private VBox VBInLeftSP;

    @FXML private Label calibrationLabel;

    @FXML private HBox HBInLeftSP;

    @FXML private Button randomBtn;

    @FXML private Button manualBtn;

    @FXML private TextField instructionToUserTF;

    @FXML private TextField userInputInCalibration;

    @FXML private Label machineInitillaziedLabel;

    @FXML private AnchorPane APRightInSP;

    @FXML private VBox VBInRightSP;

    @FXML private Label currentMachineLabel;

    @FXML private Label InitializedConfigurationLabel;

    @FXML private TextField initializeConfigurationTF;

    @FXML private Label currentConfigurationLabel;

    @FXML private TextField currentConfigurationTF;

  /*  public TabOneController(myController myController){
        this.myController = myController;
    }*/

    @FXML
    void TODOinCalibration(ActionEvent event) { /*!!!!!!!!!!!!!!!!*/

    }

    @FXML
    void actionOnManualBtn(ActionEvent event) {

    }

    @FXML
    void actionOnRandomBtn(ActionEvent event) {

         mainPageController.randomConfiguration();

         EngineFullDetailsDTO engineFullDetailsDTO = mainPageController.getEngineFullDetails();
         initializeConfigurationTF.setText(mainPageController.makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                 engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector(), engineFullDetailsDTO.getPlugBoardString()));


    }


    public void showDetails(EngineMinimalDetailsDTO engineMinimalDetailsDTO) {

        rotorsDetails.setText("used/possible : " + engineMinimalDetailsDTO.getUsedAmountOfRotors() + "/" + engineMinimalDetailsDTO.getRotorsAmount());
        reflectorsAmount.setText(String.valueOf(engineMinimalDetailsDTO.getReflectorsAmount()));
        decodedStrings.setText(String.valueOf(engineMinimalDetailsDTO.getAmountOfDecodedStrings()));
    }

    @FXML
    void onEnter(ActionEvent event) { //when the user enters 'enter', we can get the input using the method below.
        instructionToUserTF.setText(userInputInCalibration.getText());

    }

    public void setMainController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void enableButtons() {
        randomBtn.setDisable(false);
        manualBtn.setDisable(false);
    }

}
