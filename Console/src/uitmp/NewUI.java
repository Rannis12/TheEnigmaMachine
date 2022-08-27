package uitmp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class NewUI {

    @FXML
    private BorderPane borderPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab1;

    @FXML
    private AnchorPane anchorPaneT1;

    @FXML
    private Tab tab2;

    @FXML
    private AnchorPane anchorPaneT2;

    @FXML
    private Button button;

    private int count = 0;
    @FXML
    void doSome(ActionEvent event) {

        ++count;
        button.setText("now" + count);
    }






}



