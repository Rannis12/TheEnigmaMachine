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
    private Engine engine;

    @FXML
    public void initialize() {
        if(firstTabController != null && secondTabController != null){
            firstTabController.setMainController(this);
            secondTabController.setMainPageController(this);
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
    void loadFile(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            if(loadFileFromXml(file.getAbsolutePath())) {
                firstTabController.showDetails(engine.getEngineMinimalDetails());
                firstTabController.enableButtons();
            }
        }
    }


    public boolean loadFileFromXml(String fileDestination){

        try {
            if (isFileExistAndXML(fileDestination)) {
                EngineLoader engineLoader = new EngineLoader(fileDestination);
                engine = engineLoader.loadEngineFromXml(fileDestination);

                firstTabController.setReflectorsCB(getReflectorsIDs());
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

    public void enableDecodingAndClearButtons() {
        secondTabController.enableDecodingAndClearButtons();
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

    public void checkSelectedReflectorValidity(String tmpString) throws invalidInputException {
        engine.checkSelectedReflectorValidity(tmpString);
    }

    public void checkPlugBoardValidity(String tmpString, PlugBoardDTO plugBoardDTO) throws invalidInputException {
        engine.checkPlugBoardValidity(tmpString, plugBoardDTO.getUICables());
    }

    public void setNewMachine(RotorsFirstPositionDTO rotorsFirstPositionDTO, PlugBoardDTO plugBoardDTO, ReflectorDTO reflectorDTO, RotorsIndexesDTO rotorsIndexesDTO) {
        engine.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);
    }

    public ArrayList<Integer> getReflectorsIDs(){
        return engine.getReflectorsIDs();
    }
    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }



}

