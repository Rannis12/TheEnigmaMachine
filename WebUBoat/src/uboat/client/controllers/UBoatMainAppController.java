package uboat.client.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import uboat.client.login.LoginController;

import java.io.IOException;
import java.net.URL;

import static servlets.agent.utils.Constants.*;

public class UBoatMainAppController {

    @FXML private AnchorPane mainPanel;

    private UBoatController uBoatController;
    private LoginController loginController;

    private BorderPane uBoatComponent;
    private BorderPane loginComponent;

    private final StringProperty currentUserName = new SimpleStringProperty(JHON_DOE);
    @FXML public void initialize() {

        loadLoginPage();
        loadUBoatRoomPage();

        uBoatController.getUserLabel().textProperty().bind(Bindings.concat("Hello ", currentUserName));
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(UBOAT_LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setUBoatMainAppController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUBoatRoomPage() {
        URL loginPageUrl = getClass().getResource(UBOAT_ROOM_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            uBoatComponent = fxmlLoader.load();
            uBoatController = fxmlLoader.getController();
            uBoatController.setuBoatMainAppController(this);
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

    public void switchToUBoatRoom() {
        setMainPanelTo(uBoatComponent);
//        uBoatController.setActive(); //refreshing the list of chat user's in aviad's code.
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public String getUserName() {
        return currentUserName.get();
    }

    public void switchToLoginRoom() {
        setMainPanelTo(loginComponent);
    }
}
