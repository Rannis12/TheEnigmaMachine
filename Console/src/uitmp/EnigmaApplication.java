package uitmp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class EnigmaApplication extends Application {
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../resources/mainPage.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        TabPane tabPane = new TabPane(fxmlLoader.load(url.openStream()));

        MainController mainController = fxmlLoader.getController();


        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/firstTab.fxml");
        fxmlLoader.setLocation(url);
        Tab tabOne = fxmlLoader.load(url.openStream());
        TabOneController tabOneController = fxmlLoader.getController();

       /* fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/secondTab.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane tabTwo = fxmlLoader.load(url.openStream());

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("../resources/thirdTab.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane tabThree = fxmlLoader.load(url.openStream());*/

        root.setCenter(tabPane);
        tabPane.getTabs().set(0, tabOne);
        mainController.setTabs(tabOneController);



        Scene scene = new Scene(root, 500, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

