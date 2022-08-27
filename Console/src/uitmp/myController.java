package uitmp;

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

import java.io.File;

public class myController {

    private Stage stage = new Stage();

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

        // create a File chooser
        FileChooser fil_chooser = new FileChooser();

        // get the file selected
        File file = fil_chooser.showOpenDialog(stage);
        if (file != null) {

            xmlPathLabel.setText(file.getAbsolutePath()
                    + "  selected");
        }
    }
}

