package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class EnigmaApplication extends Application {
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../resources/MainPage.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

       /* fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/FirstTab.fxml");
        fxmlLoader.setLocation(url);
        Tab tabOne = fxmlLoader.load(url.openStream());
        FirstTabController firstTabController = fxmlLoader.getController();*/

       /* fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/secondTab.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane tabTwo = fxmlLoader.load(url.openStream());

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/thirdTab.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane tabThree = fxmlLoader.load(url.openStream());*/

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setTitle("The Enigma Machine!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

