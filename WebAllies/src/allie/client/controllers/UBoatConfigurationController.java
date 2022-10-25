package allie.client.controllers;

import dtos.entities.AllieDTO;
import dtos.entities.UBoatDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import okhttp3.*;
import utils.Constants;

import java.io.IOException;

import static utils.Constants.*;

public class UBoatConfigurationController {

    private DashboardController dashboardController;
    @FXML private Label battleFieldNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label statusLabel;
    @FXML private Label levelLabel;
    @FXML private Label teamsLabel;
    @FXML private Button joinBtn;

    private String activeUserName;

    private EventHandler<ActionEvent> createEventHandlerForItem() {
        return e ->
        {
            requestToJoinUBoat();
        };
    }


    @FXML
    public void initialize(){
        joinBtn.setOnAction(createEventHandlerForItem());
    }
    public void setBattleFieldNameLabel(String battleFieldNameLabel) {
        this.battleFieldNameLabel.setText(battleFieldNameLabel);
    }

    public void setUsernameLabel(String usernameLabel) {
        this.usernameLabel.setText(usernameLabel);
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel.setText(statusLabel);
    }

    public void setLevelLabel(String levelLabel) {
        this.levelLabel.setText(levelLabel);
    }

    public void setTeamsLabel(String teamsLabel) {
        this.teamsLabel.setText(teamsLabel);
    }

    public void setActiveUserName(String activeUserName) {
        this.activeUserName = activeUserName;
    }

    private void requestToJoinUBoat() {

        AllieDTO allieDTO = new AllieDTO(activeUserName);
        String json = Constants.GSON_INSTANCE.toJson(allieDTO);

        String finalUrl = HttpUrl
                .parse(FULL_SERVER_PATH + JOIN_UBOAT)
                .newBuilder()
                .addQueryParameter("username", activeUserName)
                .addQueryParameter(PARENT_NAME_PARAMETER, battleFieldNameLabel.getText())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(json.getBytes()))
                .build();

        Call call = new OkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            String respJson = response.body().string();

            UBoatDTO uBoatDTO = GSON_INSTANCE.fromJson(respJson, UBoatDTO.class);

            if(uBoatDTO.getMaxAmountOfAllies() == uBoatDTO.getCurrentAmountOfAllies()){
                joinBtn.setDisable(true);
            }

//            dashboardTab.setDisable(true); //after the allie join to the uboat, we need to lock this page, so he can't join again.
            dashboardController.setDashboardTabDisable(true);

            //refresh
            dashboardController.startBattleFieldListForContestController(uBoatDTO.getBattleName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDisable(boolean b) {
        joinBtn.setDisable(b);
    }


    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
