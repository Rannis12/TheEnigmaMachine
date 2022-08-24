package application.ui;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationUI extends Application {
    Button btn = new Button();
    Label label = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {


        BorderPane borderPane = new BorderPane();
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
    //    Label label = new Label();
        //GridPane gridPane = new GridPane();

        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-border-color: blue");
        btn.setText("check");

        vBox.getChildren().addAll(btn, label);


        FileChooser fileChooser = new FileChooser();

        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if(file != null){
                    label.setText(file.getAbsolutePath());
                    label.setStyle("-fx-background-color: purple");
                }

            }


        };
        btn.setOnAction(eventHandler);


        Scene scene = new Scene(vBox, 700, 700);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
