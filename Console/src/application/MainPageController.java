package application;

import exceptions.invalidXMLfileException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

public class MainPageController {

    @FXML private FirstTabController firstTabController;
    @FXML private SecondTabController secondTabController;
    private Engine engine;
    private Stage stage = new Stage();

    @FXML
    public void initialize() {
        if(firstTabController != null && secondTabController != null){
            firstTabController.setMainController(this);
            secondTabController.setMainPageController(this);
        }

    }

   /* private TabTwoController tabTwoController;
    private TabThreeController tabThreeController;*/
    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane anchorPaneBD;

    @FXML
    private VBox titleVB;

    @FXML
    private Label titleLabel;

    @FXML
    private GridPane gridPaneTitleVB;

    @FXML
    private Button loadFileBtn;

    @FXML
    private Label xmlPathLabel;

    @FXML
    private AnchorPane centerAP;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabOne;


    @FXML
    private AnchorPane tabOneAP;

    @FXML
    private Tab tabTwo;

    @FXML
    private AnchorPane tabTwoAP;

    @FXML
    private Tab tabThree;

    @FXML
    private AnchorPane tabThreeAP;

    @FXML
    void loadFile(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if(loadFileFromXml(file.getAbsolutePath()))
                firstTabController.showDetails(engine.getEngineMinimalDetails());

        }
    }


    public boolean loadFileFromXml(String fileDestination){

        try {
            if (isFileExistAndXML(fileDestination)) {
                EngineLoader engineLoader = new EngineLoader(fileDestination);
                engine = engineLoader.loadEngineFromXml(fileDestination);

                engine.resetStatistics();
                xmlPathLabel.setText(fileDestination + " selected");
                return true;
            }
        } catch (invalidXMLfileException e) {
            xmlPathLabel.setText(e.getMessage());
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




}

