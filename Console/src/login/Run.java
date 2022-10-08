package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Run extends Application{
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("login.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());


        Scene scene = new Scene(root, 500, 620);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(500);
        primaryStage.show();
    }

}
