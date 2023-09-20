package allie.client.controllers;

import dtos.entities.AgentDTO;
import dtos.web.ContestDetailsDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import logic.AgentsRefresher;
import logic.BattleFieldRefresher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static servlets.agent.utils.Constants.CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION;
import static servlets.agent.utils.Constants.REFRESH_RATE;

public class DashboardController {

    private AlliesController alliesController;
    ObservableList<AgentDTO> agentsDataObservableList = FXCollections.observableArrayList();
    private ObservableList<String> battleFieldNames;
    private TimerTask battleFieldListRefresher;
    private Timer timer;

    @FXML private FlowPane activeContestsFP;
    @FXML private TableView<AgentDTO> agentsTV;
    @FXML private TableColumn<?, ?> agentNameCol;
    @FXML private TableColumn<?, ?> threadsCol;
    @FXML private TableColumn<?, ?> missionsCol;
    private Timer tableViewTimer;

    public DashboardController(){
        battleFieldNames = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize(){

        agentNameCol.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        threadsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfThreads"));
        missionsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfMissions"));
        agentsDataObservableList = FXCollections.observableArrayList();

        agentsTV.setItems(agentsDataObservableList);

//        ContestsCB.setItems(battleFieldNames); //maybe cause problems.
    }

    public void setAlliesController(AlliesController alliesController) {
        this.alliesController = alliesController;
    }

    private void updateBattleFieldList(List<ContestDetailsDTO> dtoList) {
        Platform.runLater(() -> {
            updateFlowPane(dtoList);
        });
    }

    private void updateFlowPane(List<ContestDetailsDTO> dtoList) {

        activeContestsFP.getChildren().clear();
        for (ContestDetailsDTO dto : dtoList) {

            if(dto != null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource(CONTEST_DATA_PAGE_FXML_RESOURCE_LOCATION);
                fxmlLoader.setLocation(url);
                try {
                    Parent root = fxmlLoader.load(url.openStream());
                    UBoatConfigurationController controller = fxmlLoader.getController();

                    controller.setBattleFieldNameLabel(dto.getBattleFieldName());
                    controller.setUsernameLabel(dto.getUsername());
                    if(dto.getGameStatus()){
                        controller.setStatusLabel("Ready");
                    }else{
                        controller.setStatusLabel("Not Ready");
                    }

                    controller.setLevelLabel(dto.getDifficulty());
                    controller.setTeamsLabel(dto.getTeams());
                    controller.setActiveUserName(alliesController.getCurrentUserName());
                    controller.setDashboardController(this);

                    if(dto.getCurrentAmountOfTeams() == dto.getMaxAmountOfTeams()){
                        controller.setDisable(true);
                    }

                    activeContestsFP.getChildren().add(root);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public void startBattleFieldListRefresher() {
        battleFieldListRefresher = new BattleFieldRefresher(
                this::updateBattleFieldList);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setDashboardTabDisable(boolean b) {
        alliesController.setDashboardTabDisable(b);
    }

    public void startBattleFieldListForContestController(String battleName) {
        alliesController.startBattleFieldListForContestController(battleName);
    }

    private void updateAgentsTable(List<AgentDTO> dtoList) {
        Platform.runLater(() -> {
            updateTableView(dtoList);
        });
    }

    private void updateTableView(List<AgentDTO> dtoList) {

        agentsDataObservableList.clear();

        for (AgentDTO dto : dtoList) {
            if(dto != null){
                agentsDataObservableList.add(dto);
            }
        }
    }

    public void startTableViewRefresher() {
        battleFieldListRefresher = new AgentsRefresher(
                this::updateAgentsTable);
        tableViewTimer = new Timer();
        tableViewTimer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    public void resetController() { // maybe need more?
        agentsDataObservableList.clear();
        activeContestsFP.getChildren().clear();

    }
}
