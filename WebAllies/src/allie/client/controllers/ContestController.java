package allie.client.controllers;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.MissionDTO;
import dtos.TeamInformationDTO;
import dtos.entities.AgentDTO;
import dtos.web.ContestDetailsDTO;
import dtos.web.ContestProgressDTO;
import dtos.web.ShouldStartContestDTO;
import exceptions.invalidInputException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import logic.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import servlets.agent.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static servlets.agent.utils.Constants.*;

public class ContestController {

    private AlliesController alliesController;
    private ObservableList<AgentDTO> agentsDataObservableList;
    private ObservableList<MissionDTO> candidatesDataObservableList;
    private TimerTask battleFieldListRefresher;
    private TimerTask candidatesRefresher;
    private Timer timer;
    @FXML private TextField battleNameTF;
    @FXML private TextField usernameTF;
    @FXML private TextField statusTF;
    @FXML private TextField levelTF;
    @FXML private TextField currentTeamsTF;
    @FXML private FlowPane teamInformationFP;
    @FXML private Button readyBtn;
    @FXML private Button finishBtn;
    @FXML private Button clearBtn;
    @FXML private TextField missionSizeTF;
    @FXML private TextField toEncodeTF;
    @FXML private TableView<AgentDTO> agentsTable;
    @FXML private TextField totalMissionsAmountTF;
    @FXML private TextField missionsInQueueTF;
    @FXML private TextField totalMissionsFinishedTF;

    @FXML private TableColumn<?, ?> agentNameCol;
    @FXML private TableColumn<?, ?> missionsCol;
    @FXML private TableColumn<?, ?> candidatesCol;

    @FXML private TableView<MissionDTO> candidatesTV;
    @FXML private TableColumn<?, ?> encryptedStringCol;
    @FXML private TableColumn<?, ?> allieNameCol;
    @FXML private TableColumn<?, ?> rotorsCol;
    @FXML private TableColumn<?, ?> reflectorCol;
    @FXML private TableColumn<?, ?> agentCol;
    @FXML private Label winnerMessage;

    private Timer teamInformationTimer;
    private Timer tableViewTimer;
    private Timer contestRefresherTimer;
    BooleanProperty isAllieReady = new SimpleBooleanProperty();
    private BooleanProperty isContestStart = new SimpleBooleanProperty();
    private BooleanProperty isLogoutClicked = new SimpleBooleanProperty();

    private BooleanProperty didUBoatLoggedOut = new SimpleBooleanProperty();
    private Timer candidatesTimer;
    private IsContestEndRefresher contestEndRefresher;
    private Timer contestEndTimer;
    private GetContestProgressRefresher contestProgress;
    private Timer contestProgressTimer;
    private IntegerProperty totalMissionsAmount;
    private IntegerProperty missionsInQueue;
    private IntegerProperty totalMissionsFinished;

    private DidUBoatLoggedOutRefresher logoutRefresher;
    private Timer logoutTimer;

    public ContestController(){

    }

    @FXML
    public void initialize(){
        isAllieReady.set(false);
        isContestStart.set(false);
        readyBtn.setDisable(false);

        readyBtn.setTextFill(Color.RED);

        isAllieReady.addListener(e ->{
            if(isAllieReady.getValue()){
                readyBtn.setTextFill(Color.GREEN);
            }else{
                readyBtn.setTextFill(Color.RED);
            }
        });

        isContestStart.addListener(e ->{
            if(isContestStart.getValue()) {
                readyBtn.setDisable(true);

                getContestDetails();
                startCreatingMissions();
                startGetCandidatesRefresher();
                checkIfContestIsEnd();
            }
        });

        didUBoatLoggedOut.addListener(e -> {
            if (didUBoatLoggedOut.getValue()){
                finishBtn.setVisible(true);
                clearBtn.setVisible(false);

            }
        });

        totalMissionsAmount = new SimpleIntegerProperty();
        missionsInQueue = new SimpleIntegerProperty();
        totalMissionsFinished = new SimpleIntegerProperty();

        initAgentsTable();
        initCandidatesTable();
    }

    private void resetContestController() {

        isContestStart.set(false);
        isAllieReady.set(false);
        readyBtn.setDisable(false);
        finishBtn.setVisible(false);
        clearBtn.setVisible(false);

        unbindToProgressAndClearTF();

        winnerMessage.setText("");

        missionSizeTF.clear();
        toEncodeTF.clear();

        if(isLogoutClicked.get()) {
            clearContestDetails();
            isLogoutClicked.set(false);
        }

        candidatesDataObservableList.clear();
        agentsDataObservableList.clear();
    }

    private void clearContestDetails() {
        battleNameTF.clear();
        usernameTF.clear();
        statusTF.clear();
        levelTF.clear();
        currentTeamsTF.clear();
    }

    public void unbindToProgressAndClearTF(){
        totalMissionsAmountTF.textProperty().unbind();
        totalMissionsAmountTF.clear();
        missionsInQueueTF.textProperty().unbind();
        missionsInQueueTF.clear();
        totalMissionsFinishedTF.textProperty().unbind();
        totalMissionsFinishedTF.clear();
    }
    public void bindToProgress() {
        totalMissionsAmountTF.textProperty().bind(totalMissionsAmount.asObject().asString());
        missionsInQueueTF.textProperty().bind(missionsInQueue.asObject().asString());
        totalMissionsFinishedTF.textProperty().bind(totalMissionsFinished.asObject().asString());
    }

    private void initCandidatesTable() {
        encryptedStringCol.setCellValueFactory(new PropertyValueFactory<>("DecodedTo"));
        allieNameCol.setCellValueFactory(new PropertyValueFactory<>("allieName"));
        rotorsCol.setCellValueFactory(new PropertyValueFactory<>("rotorsPositionsAndOrder"));
        reflectorCol.setCellValueFactory(new PropertyValueFactory<>("chosenReflector"));
        agentCol.setCellValueFactory(new PropertyValueFactory<>("agentID"));

        candidatesDataObservableList = FXCollections.observableArrayList();

        candidatesTV.setItems(candidatesDataObservableList);

    }

    @FXML
    void readyBtnListener(ActionEvent event) {

            try {
                try {
                    Integer.parseInt(missionSizeTF.getText());
                }catch(InputMismatchException | NumberFormatException e){
                    throw new invalidInputException("Please enter a Positive number.");
                }

            if (battleNameTF.getText().equals("")) { //yet registered to uboat
                throw new invalidInputException("Please select a tournament first.");
            }
            if(missionSizeTF.getText().equals("")){     //need to check that is a integer //todo
                throw new invalidInputException("Please select amount of missions.");
            }

            isAllieReady.set(!isAllieReady.getValue());
            String status = fromBooleanToString(isAllieReady.getValue());

            String finalUrl = HttpUrl
                    .parse(ALLY_POST_HIS_STATUS_PATH)
                    .newBuilder()
                    .addQueryParameter("username", alliesController.getCurrentUserName())
                    .addQueryParameter("status", status)
                    .addQueryParameter("battleField", battleNameTF.getText())
                    .build()
                    .toString();

            String json = GSON_INSTANCE.toJson(missionSizeTF.getText(), String.class);

            HttpClientUtil.runAsyncPost(finalUrl, json, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Error: " + e.getMessage());
                        alliesController.popUpError(e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (response.code() != 200) {
                        Platform.runLater(() -> {
                            alliesController.popUpError("Error: " + responseBody);
                            isAllieReady.set(false);
                            readyBtn.setTextFill(Color.RED);
                        });
                    } //else {
                        //Platform.runLater(() -> {
                          //  uBoatMainAppController.updateUserName(userName);
                            //uBoatMainAppController.switchToUBoatRoom();
                        //});
                   // }
                }
            });
        }catch (invalidInputException e){
            alliesController.popUpError(e.getMessage());
        }

    }

    @FXML
    void finishBtnListener(ActionEvent event) {

        isLogoutClicked.set(true);
        String finalUrl = HttpUrl
                .parse(Constants.CONFIRMED_LOGOUT_PATH)
                .newBuilder()
                .addQueryParameter("allyName", alliesController.getCurrentUserName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
                    alliesController.popUpError(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        alliesController.popUpError("Error: " + responseBody);
                    });
                } else {
                Platform.runLater(() -> {
                    resetContestController();
                    alliesController.resetDashboardController();
                });
                 }
            }
        });

    }

    @FXML
    void clearBtnListener(ActionEvent event) {

        String finalUrl = HttpUrl
                .parse(Constants.CONFIRMED_CLEAR_PATH)
                .newBuilder()
                .addQueryParameter("allyName", alliesController.getCurrentUserName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
                    alliesController.popUpError(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        alliesController.popUpError("Error: " + responseBody);
                    });
                } else {
                    Platform.runLater(() -> {
                        resetContestController();
                        alliesController.startBattleFieldListForContestController(battleNameTF.getText()); // may cause problems
//                        alliesController.resetDashboardController();
                    });
                }
            }
        });
    }


    private String fromBooleanToString(Boolean value) {
        if(value){
            return "Ready";
        }
        return "Not Ready";
    }

    private void initAgentsTable() {

        agentNameCol.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        candidatesCol.setCellValueFactory(new PropertyValueFactory<>("amountOfCandidates"));
        missionsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfMissions"));
        agentsDataObservableList = FXCollections.observableArrayList();

        agentsTable.setItems(agentsDataObservableList);

//        ContestsCB.setItems(battleFieldNames); //maybe cause problems.
    }


    public void setAlliesController(AlliesController alliesController){
        this.alliesController = alliesController;
    }


    public void showContestDetails(ContestDetailsDTO dto) {

        resetContestDetails();
        battleNameTF.setText(dto.getBattleFieldName());
        usernameTF.setText(dto.getUsername());
        if(dto.getGameStatus()){
            statusTF.setText("Ready");
        }else{
            statusTF.setText("Not Ready");
        }

        levelTF.setText(dto.getDifficulty());

//        String teams = String.valueOf(uBoatDTO.getCurrentAmountOfAllies()) + '/' + uBoatDTO.getMaxAmountOfAllies();
        currentTeamsTF.setText(dto.getTeams());
    }


    private void updateBattleFieldList(ContestDetailsDTO dto) {
        Platform.runLater(() -> {
            showContestDetails(dto);
        });
    }


    public void startBattleFieldListRefresher(String battleName) {
        battleFieldListRefresher = new SoloBattleFieldRefresher(
                this::updateBattleFieldList,
                battleName);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void resetContestDetails(){
        battleNameTF.clear();
        usernameTF.clear();
        statusTF.clear();
        levelTF.clear();
        currentTeamsTF.clear();
    }

    public void startTeamInformationRefresher(String battleName) {
        battleFieldListRefresher = new TeamInformationRefresher(
                this::updateTeamFlowPane,
                battleName);
        teamInformationTimer = new Timer();
        teamInformationTimer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void showTeamInformationDetails(List<TeamInformationDTO> dtosList) {

        teamInformationFP.getChildren().clear();
        //clear Flow pane and then add.

        for (TeamInformationDTO dto : dtosList) {
            if (dto != null) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource(TEAM_DETAILS_PAGE_FXML_RESOURCE_LOCATION);
                fxmlLoader.setLocation(url);
                try {
                    Parent root = fxmlLoader.load(url.openStream());
                    TeamDetailsController controller = fxmlLoader.getController();

                    controller.setTeamNameLabel(dto.getAllieName());
                    controller.setAgentAmountLabel(String.valueOf(dto.getAmountOfAgent()));
                    controller.setMissionSizeLabel(String.valueOf(dto.getMissionsSize()));

                    teamInformationFP.getChildren().add(root);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void updateTeamFlowPane(List<TeamInformationDTO> dtoSet) {
        Platform.runLater(() -> {
            showTeamInformationDetails(dtoSet);
        });
    }


    private void updateAgentsTableViewRefresher(List<AgentDTO> dtoList) {
        Platform.runLater(() -> {
            insertToAgentTableView(dtoList);
        });
    }

    private void insertToAgentTableView(List<AgentDTO> dtoList) {

        agentsDataObservableList.clear();

        for (AgentDTO dto : dtoList) {
            if(dto != null){
                agentsDataObservableList.add(dto);
            }
        }
    }

    public void startAgentsTableViewRefresher() {
        battleFieldListRefresher = new AgentsRefresher(
                this::updateAgentsTableViewRefresher);
        tableViewTimer = new Timer();
        tableViewTimer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void updateToEncodeTextFieldRefresher(ShouldStartContestDTO contestDTO) {
        Platform.runLater(() -> {
            startContestRefresher(contestDTO);
        });
    }

    private void startContestRefresher(ShouldStartContestDTO contestDTO) {

        if (contestDTO != null) {
            if (contestDTO.isShouldStart()) {
                toEncodeTF.setText(contestDTO.getToEncode());

                isContestStart.set(true);
                //stop refreshing some components after everyone is ready.
                contestRefresherTimer.cancel();
//                teamInformationTimer.cancel();
                battleFieldListRefresher.cancel();
                tableViewTimer.cancel();
            }
        }
    }

    private void startCreatingMissions() {
        String finalUrl = HttpUrl
                .parse(Constants.START_CREATE_MISSIONS_PATH)
                .newBuilder()
                .addQueryParameter("username", alliesController.getCurrentUserName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                alliesController.popUpError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    public void startShouldStartContestRefresher(String battleName) {
        battleFieldListRefresher = new ContestStatusRefresher(
                this::updateToEncodeTextFieldRefresher,
                battleName,
                "",
                "Ally");
        contestRefresherTimer = new Timer();
        contestRefresherTimer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void updateCandidatesTableView(List<MissionDTO> dtoList) {
        Platform.runLater(() -> {
            insertToCandidatesTableView(dtoList);
        });
    }

    private void insertToCandidatesTableView(List<MissionDTO> dtoList) {

        candidatesDataObservableList.clear();

        for (MissionDTO dto : dtoList) {
            if(dto != null){
                candidatesDataObservableList.add(dto);
            }
        }
    }

    public void startGetCandidatesRefresher() {
        candidatesRefresher = new GetCandidatesRefresher(
                this::updateCandidatesTableView,
                alliesController.getCurrentUserName(),
                "Ally");
        candidatesTimer = new Timer();
        candidatesTimer.schedule(candidatesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startIsContestEndRefresher(MissionDTO missionDTO) {
        Platform.runLater(() -> {
            isThereAWinner(missionDTO);
        });
    }


    private void isThereAWinner(MissionDTO missionDTO) {

        if (missionDTO != null) {
            if (missionDTO.isWinner()) {

                contestEndTimer.cancel();
//                battleFieldListRefresher.cancel();
                timer.cancel();

                winnerMessage.setText(missionDTO.getAgentID() + " was the first to found!");

                candidatesTimer.cancel();

                contestProgress.cancel();
                contestProgressTimer.cancel();

                clearBtn.setVisible(true);
                startCheckIfUBoatLoggedOut();
            }
        }
    }

    public void checkIfContestIsEnd() {
        contestEndRefresher = new IsContestEndRefresher(
                this::startIsContestEndRefresher, "",
                alliesController.getCurrentUserName(),
                "Ally");
        contestEndTimer = new Timer();
        contestEndTimer.schedule(contestEndRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void ContestProgressRefresher(ContestProgressDTO contestProgressDTO) {
        Platform.runLater(() -> {
            updateContestProgress(contestProgressDTO);
        });
    }

    private void updateContestProgress(ContestProgressDTO contestProgressDTO) {

        if (contestProgressDTO != null) {

            totalMissionsAmount.set(contestProgressDTO.getTotalAmountOfMissions());
            missionsInQueue.set(contestProgressDTO.getMissionsInQueue());
            totalMissionsFinished.set(totalMissionsAmount.getValue() - missionsInQueue.getValue());
        }
    }

    public void getContestDetails() {
        contestProgress = new GetContestProgressRefresher(
                this::ContestProgressRefresher,
                alliesController.getCurrentUserName());
        contestProgressTimer = new Timer();
        contestProgressTimer.schedule(contestProgress, REFRESH_RATE, REFRESH_RATE);
    }


    private void DidUBoatLoggedOutRefresher(boolean value) {
        Platform.runLater(() -> {
            loggedOutAction(value);
        });
    }

    private void loggedOutAction(boolean value) {
        if (value) {
            didUBoatLoggedOut.set(true);
//            battleFieldListRefresher.cancel();
            logoutRefresher.cancel();
            logoutTimer.cancel();
        }
    }

    public void startCheckIfUBoatLoggedOut() {
        logoutRefresher = new DidUBoatLoggedOutRefresher(
                this::DidUBoatLoggedOutRefresher,
                alliesController.getCurrentUserName(),
                "Ally");
        logoutTimer = new Timer();
        logoutTimer.schedule(logoutRefresher, 1000, 1000);
    }


    public void stateReadyButton(boolean b) {
        readyBtn.setDisable(b);
    }
}
