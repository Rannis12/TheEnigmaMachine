package agent.client.controllers;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xpath.internal.operations.Bool;
import decryption.DecryptTask;
import decryption.dm.DecryptionManager;
import dtos.DecryptionManagerDTO;
import dtos.web.DecryptTaskDTO;
import dtos.web.ShouldStartContestDTO;
import exceptions.invalidXMLfileException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.ScheduledService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import logic.ContestStatusRefresher;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ThreadPoolExecutor;

import static utils.Constants.*;

public class AgentController {

    public static Object queueLock = new Object();
    private InputStream inputStream;
    private Engine engine;
    private DecryptionManager decryptionManager;
    private AgentMainAppController agentMainAppController;
    @FXML private Label userLabel;
    @FXML private Label check;
    private Timer contestRefresherTimer;
    private ContestStatusRefresher battleFieldListRefresher;
    BooleanProperty isContestStart = new SimpleBooleanProperty(false);
    private int amountOfThreads;
    private int amountOfMissions;

    private BooleanProperty canTakeMissions = new SimpleBooleanProperty();
    @FXML
    public void initialize(){

        isContestStart.addListener(e -> {
            if(isContestStart.getValue()){
                startContest();

//                decryptionManager = new DecryptionManager(new DecryptionManagerDTO(), engine);
            }
        });
    }

    private void startContest() {
        setEngine();
        startPollMissions();
//        checkQueueStatus();

    }

    private void startPollMissions() {
        new Thread(() -> {
            String finalUrl = HttpUrl
                    .parse(Constants.START_POLL_MISSIONS_PATH)
                    .newBuilder()
                    .addQueryParameter("username", agentMainAppController.getCurrentUserName())
                    .addQueryParameter("amountOfMissions", String.valueOf(amountOfMissions))
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    agentMainAppController.popUpError(e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonDto = response.body().string();
                    DecryptTaskDTO[] dtos = GSON_INSTANCE.fromJson(jsonDto, DecryptTaskDTO[].class);

                    putMissionsInThreadPool(Arrays.asList(dtos));
//                consumer.accept(dtos);
                }
            });
        }).start();

    }

    private void putMissionsInThreadPool(List<DecryptTaskDTO> dtos) {
        int size = dtos.size();

        for (int i = 0; i < size; i++) {
//            threadPool.submit(new DecryptTask())

        }
    }

    public void setEngine(){

        String finalUrl = HttpUrl
                .parse(FULL_SERVER_PATH + "/get-fileContent")
                .newBuilder()
                .addQueryParameter("agentName", agentMainAppController.getCurrentUserName())
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
                    popUpError(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        popUpError("Error: " + responseBody);

                    });
                } else {
                    Platform.runLater(() -> {
                        inputStream = new ByteArrayInputStream(responseBody.getBytes());
                        try {
                            EngineLoader engineLoader = new EngineLoader();
                            engine = engineLoader.loadEngineFromInputStream(inputStream);

                        } catch (invalidXMLfileException e) {
                            popUpError(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Oops, something went wrong...");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }

    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.agentMainAppController = agentMainAppController;
    }

    public Label getUserLabel() {
        return userLabel;
    }


    private void updateToEncodeTextFieldRefresher(ShouldStartContestDTO contestDTO) {
        Platform.runLater(() -> {
            startContestRefresher(contestDTO);
        });
    }

    private void startContestRefresher(ShouldStartContestDTO contestDTO) {

        if (contestDTO != null) {
            if (contestDTO.isShouldStart()) {

                isContestStart.set(true);
                //stop refreshing some components after everyone is ready.
                contestRefresherTimer.cancel();

            }
        }
    }

    public void startShouldStartContestRefresher() {
        battleFieldListRefresher = new ContestStatusRefresher(
                this::updateToEncodeTextFieldRefresher, "",
                agentMainAppController.getCurrentUserName(),
                "Agent");
        contestRefresherTimer = new Timer();
        contestRefresherTimer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setActive() {
        startShouldStartContestRefresher();
    }

    public void setAmountOfThreads(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }

    public void setAmountOfMissions(int amountOfMissions){
        this.amountOfMissions = amountOfMissions;
    }


    private void takeMissions(){
        if(canTakeMissions.get()){
            synchronized (queueLock){
                if(canTakeMissions.get()){
                    try{
                        queueLock.wait();
                    }catch (InterruptedException ignored){

                    }
                }
            }
        }
    }

}
