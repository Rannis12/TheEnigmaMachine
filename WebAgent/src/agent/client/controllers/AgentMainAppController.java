package agent.client.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import agent.client.login.LoginController;

import java.io.IOException;
import java.net.URL;

import static servlets.agent.utils.Constants.AGENT_LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static servlets.agent.utils.Constants.AGENT_ROOM_PAGE_FXML_RESOURCE_LOCATION;

public class AgentMainAppController {

    @FXML private AnchorPane mainPanel;
    @FXML private LoginController loginController;
    @FXML private AgentController agentController;
    private AnchorPane agentComponent;
    private BorderPane loginComponent;

    private StringProperty currentUserName = new SimpleStringProperty();

    @FXML public void initialize() {

        loadLoginPage();
        loadAlliesRoomPage();

        agentController.getUserLabel().textProperty().bind(Bindings.concat("Hello ", currentUserName));
    }

   private void loadAlliesRoomPage() {

        URL loginPageUrl = getClass().getResource(AGENT_ROOM_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            agentComponent = fxmlLoader.load();
            agentController = fxmlLoader.getController();
            agentController.setAgentMainAppController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(AGENT_LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setAgentMainAppController(this);
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

    public void switchToAgentRoom() {
        setMainPanelTo(agentComponent);
        agentController.setActive();

        agentController.setAmountOfThreads(loginController.getAmountOfThreads());
        agentController.setAmountOfMissions(loginController.getAmountOfMissions());
        agentController.setAllieName(loginController.getAllieName());

//        agentController.setThreadPool();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public String getCurrentUserName(){
        return currentUserName.get();
    }

    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();

    }
    public String getAllieName() {
        return loginController.getAllieName();
    }

    public void switchToLoginRoom() {
        setMainPanelTo(loginComponent);
        loginController.startBattleFieldListRefresher();

    }
}
