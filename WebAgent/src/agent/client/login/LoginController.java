package agent.client.login;

import agent.client.controllers.AgentMainAppController;
import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.entities.AgentDTO;
import dtos.web.ContestDetailsDTO;
import exceptions.invalidInputException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.BattleFieldRefresher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static servlets.agent.utils.Constants.GSON_INSTANCE;

public class LoginController {

    private AgentMainAppController agentMainAppController;
    private TimerTask battleFieldListRefresher;
    private Timer timer;
    @FXML private TextField nameTF;
    @FXML private ChoiceBox<String> alliesCB;
    @FXML private Spinner<Integer> threadsSpinner;
    @FXML private Spinner<Integer> missionsSpinner;
    @FXML private Label errorMessageLabel;
    @FXML private Button loginBtn;

    private String allieName;
    private StringProperty errorMessageProperty = new SimpleStringProperty();


    @FXML public void initialize(){
        errorMessageLabel.textProperty().bind(errorMessageProperty);

        startBattleFieldListRefresher();

        //setting spinners
        SpinnerValueFactory<Integer> threadsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,4);
        threadsValueFactory.setValue(1);
        threadsSpinner.setValueFactory(threadsValueFactory);

        SpinnerValueFactory<Integer> MissionsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        MissionsValueFactory.setValue(1);
        missionsSpinner.setValueFactory(MissionsValueFactory);
    }

    @FXML
    void loginBtnListener(ActionEvent event) {

        try{
            String userName = nameTF.getText();
            allieName = alliesCB.getValue();

            if(userName.equals("")){
                throw new invalidInputException("Please enter a valid name.");
            }
            if(allieName == null){
                throw new invalidInputException("please select allie.");
            }

            AgentDTO agentDTO = new AgentDTO(nameTF.getText(), allieName,
                    threadsSpinner.getValue(), missionsSpinner.getValue(), 0, 0);

            //request to join the allie team.

            String finalUrl = HttpUrl
                    .parse("http://localhost:8080/webApp/post-agent")
                    .newBuilder()
                    .addQueryParameter("username", userName)
                    .build()
                    .toString();

            String json = GSON_INSTANCE.toJson(agentDTO, AgentDTO.class);

            HttpClientUtil.runAsyncPost(finalUrl, json, new Callback() { //logic is working. need to change the way it looks in the application.

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Error: " + e.getMessage());
                        errorMessageProperty.set("Error: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                errorMessageProperty.set("Error: " + responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            agentMainAppController.updateUserName(userName);
                            agentMainAppController.switchToAgentRoom();
                        });
                    }
                }
            });


        }catch (invalidInputException e){
            agentMainAppController.popUpError(e.getMessage());
        }
    }

    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.agentMainAppController = agentMainAppController;
    }

    private void updateBattleFieldList(List<ContestDetailsDTO> dtoList) {
        Platform.runLater(() -> {
            updateChoiceBox(dtoList);
        });
    }

    private synchronized void updateChoiceBox(List<ContestDetailsDTO> dtoList) {

        alliesCB.getItems().clear();
        ObservableList observableList = FXCollections.observableArrayList();
        for (ContestDetailsDTO dto : dtoList) {
            if(dto != null){
                for (String name : dto.getAlliesNames()) {
                    observableList.add(name);
                }
            }
        }
        alliesCB.setItems(observableList);
    }

    public void startBattleFieldListRefresher() {
        battleFieldListRefresher = new BattleFieldRefresher(
                this::updateBattleFieldList);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, 5000, 5000);
    }

    public int getAmountOfMissions(){
        return missionsSpinner.getValue();
    }

    public int getAmountOfThreads(){
        return threadsSpinner.getValue();
    }

    public String getAllieName() {
        return allieName;
    }
}
