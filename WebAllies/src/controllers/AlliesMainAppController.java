package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import login.LoginController;

import java.io.IOException;
import java.net.URL;

import static util.Constants.*;


public class AlliesMainAppController {

    @FXML private AnchorPane mainPanel;
    private LoginController loginController;
    private AlliesController alliesController;
    private BorderPane alliesComponent;
    private BorderPane loginComponent;

    private StringProperty currentUserName = new SimpleStringProperty();

    @FXML public void initialize() {

        loadLoginPage();
        loadAlliesRoomPage();

        alliesController.getUserLabel().textProperty().bind(Bindings.concat("Hello ", currentUserName));
    }

    private void loadAlliesRoomPage() {

        URL loginPageUrl = getClass().getResource(ALLIES_ROOM_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            alliesComponent = fxmlLoader.load();
            alliesController = fxmlLoader.getController();
            alliesController.setAlliesMainAppController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(ALLIES_LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setAlliesMainAppController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToAlliesRoom() {
        setMainPanelTo(alliesComponent);
//        uBoatController.setActive(); //refreshing the list of chat user's in aviad's code.
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }
}
