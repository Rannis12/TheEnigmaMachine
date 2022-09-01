package application;

import dtos.EngineFullDetailsDTO;
import exceptions.invalidXMLfileException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainPageController {

    @FXML private FirstTabController firstTabController;
    @FXML private SecondTabController secondTabController;

    //private ErrorApplication errorApp;
    private Engine engine;
    private Stage stage = new Stage();

    @FXML
    public void initialize() {
        if(firstTabController != null && secondTabController != null){
            firstTabController.setMainController(this);
            secondTabController.setMainPageController(this);

            //ErrorMsgController.setMainPageController(this);
        }

    }

    @FXML
    private VBox titleVB;

    @FXML
    private Label titleLabel;

    @FXML
    private Button loadFileBtn;

    @FXML
    private Label xmlPathLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabOne;


    @FXML
    private Tab tabTwo;

    @FXML
    private Tab tabThree;

    @FXML
    void loadFile(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if(loadFileFromXml(file.getAbsolutePath())) {
                firstTabController.showDetails(engine.getEngineMinimalDetails());
                firstTabController.enableButtons();
            }
        }
    }


    public boolean loadFileFromXml(String fileDestination) throws IOException {

        try {
            if (isFileExistAndXML(fileDestination)) {
                EngineLoader engineLoader = new EngineLoader(fileDestination);
                engine = engineLoader.loadEngineFromXml(fileDestination);

                engine.resetStatistics();
                xmlPathLabel.setText(fileDestination + " selected");
                return true;
            }
        } catch (invalidXMLfileException e) {
            popUpError(e.getMessage());
        }
        return false;
    }

    public boolean isFileExistAndXML(String fullPath) throws invalidXMLfileException {
        if(fullPath.endsWith(".xml")){
            File tmpFile = new File(fullPath);
            if(tmpFile.exists()){
                return true;
            }
            else{
                throw new invalidXMLfileException("File doesn't exists.");
            }
        }
        else{
            throw new invalidXMLfileException(fullPath +" isn't a xml file.");
        }
    }

    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
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


    public void setTabsConfiguration(String newConfiguration) {
        firstTabController.setCurrentConfigurationLabel(newConfiguration);
        secondTabController.setCurrentConfigurationLabel(newConfiguration);
        //thirdTabController.setCurrentConfigurationLabel(newConfiguration);
    }
}

