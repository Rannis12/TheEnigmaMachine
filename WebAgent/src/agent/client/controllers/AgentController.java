package agent.client.controllers;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import dtos.DecodeStringInfo;
import dtos.MissionDTO;
import dtos.entities.AgentDTO;
import dtos.web.ContestDetailsForAgentDTO;
import dtos.web.DataToAgentEngineDTO;
import dtos.web.DecryptTaskDTO;
import dtos.web.ShouldStartContestDTO;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import logic.ContestStatusRefresher;
import logic.IsContestEndRefresher;
import logic.enigma.Dictionary;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import okhttp3.*;
import utils.Constants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static utils.Constants.*;

public class AgentController {

    public static Object queueLock = new Object();
    public static Object putMissionLock = new Object();

    private InputStream inputStream;
    private Engine engine;
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(1000);
    private AgentMainAppController agentMainAppController;
    @FXML private Label userLabel;
    @FXML private Label battleFieldNameLabel;
    @FXML private Label contestLevelLabel;
    @FXML private Label teamsLabel;
    @FXML private Label allieNameLabel;
    @FXML private FlowPane candidatesFP;
    @FXML private Label withdrawMissionAmountLabel;
    @FXML private Label missionInQueueLabel;
    @FXML private Label contestStatusLabel;
    @FXML private Label candidatesAmountLabel;
    private Timer contestRefresherTimer;
    private ContestStatusRefresher battleFieldListRefresher;
    BooleanProperty isActiveContest = new SimpleBooleanProperty(false);
    private int amountOfThreads;
    private int amountOfMissions;
    private String toEncode;
    private IntegerProperty amountOfMissionsInQueue = new SimpleIntegerProperty();
    private IntegerProperty amountOfCompletedMissionsSoFar = new SimpleIntegerProperty();
    private int amountOfDecoding = 0;
    private String allieName;
    private IsContestEndRefresher contestEndRefresher;
    private Timer contestEndTimer;
    private IntegerProperty candidatesAmount = new SimpleIntegerProperty();
//    private PublishAgentDetailsRefresher publishAgentDetails;
//    private Timer agentDetailsTimer;

    @FXML
    public void initialize(){

        isActiveContest.addListener(e -> {
            if(isActiveContest.getValue()){
                contestStatusLabel.setText("Ready");
                startContest();
                checkIfContestIsEnd();
                startPublishAgentDetails();
            }
            else {
                contestStatusLabel.setText("Not Ready");
            }
        });



    }

    private void startContest() {

        setEngine();
        startPollMissionsThread();
        threadPool.prestartAllCoreThreads();

        withdrawMissionAmountLabel.textProperty().bind(amountOfCompletedMissionsSoFar.asObject().asString());
        missionInQueueLabel.textProperty().bind(amountOfMissionsInQueue.asObject().asString());
        candidatesAmountLabel.textProperty().bind(candidatesAmount.asObject().asString());

    }

    private void startPollMissionsThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isActiveContest.get()){
                    synchronized (queueLock){
                        System.out.println("about to poll some missions.");
                        pollMissions();
                        if(!blockingQueue.isEmpty()){
                            try{
                                System.out.println("now waiting.");
                                queueLock.wait();
                            }catch (InterruptedException e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });

        thread.start();
    }

    private void pollMissions() {

        String finalUrl = HttpUrl
                .parse(Constants.START_POLL_MISSIONS_PATH)
                .newBuilder()
                .addQueryParameter("username", agentMainAppController.getCurrentUserName())
                .addQueryParameter("amountOfMissions", String.valueOf(amountOfMissions))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            String jsonDto = response.body().string();
            DecryptTaskDTO[] dtos = GSON_INSTANCE.fromJson(jsonDto, DecryptTaskDTO[].class);

//            System.out.println("got : " + dtos.length + " missions.");

            putMissionsInThreadPool(Arrays.asList(dtos));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void putMissionsInThreadPool(List<DecryptTaskDTO> dtos) {
        if(dtos != null) {

            int size = dtos.size();

            synchronized (putMissionLock) {
                for (int i = 0; i < size; i++) {
//                    System.out.println("about to submit task into threadPool");
                    DecryptTaskDTO decryptTask = dtos.get(i);
                    if (decryptTask != null) {
                        threadPool.submit(new DecryptTask(
                                (Engine) engine.clone(),
                                decryptTask.getSizeOfMission(),
                                toEncode,
                                decryptTask.getInitialPositions()));
                    }
                }

                Platform.runLater(() ->{
                    amountOfMissionsInQueue.set(amountOfMissionsInQueue.get() + size);
                });

            }
        }
    }

    public void setEngine() {

        String finalUrl = HttpUrl
                .parse(FULL_SERVER_PATH + "/get-fileContent")
                .newBuilder()
                .addQueryParameter("agentName", agentMainAppController.getCurrentUserName())
                .build()
                .toString();


        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            String responseBody = response.body().string();

            DataToAgentEngineDTO dto = GSON_INSTANCE.fromJson(responseBody, DataToAgentEngineDTO.class);

            inputStream = new ByteArrayInputStream(dto.getInputStreamAsString().getBytes());
            try {
                EngineLoader engineLoader = new EngineLoader();
                engine = engineLoader.loadEngineFromInputStream(inputStream);

                engine.initRotorIndexes(dto.getUsedRotorsOrganization());
                engine.initSelectedReflector(dto.getSelectedReflector());

//                engine.setNotchesCurrentPlaces(dto.getNotchesCurrentPlaces());


            } catch (invalidXMLfileException e) {
                popUpError(e.getMessage());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

                toEncode = contestDTO.getToEncode();
                isActiveContest.set(true);
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

    public void setAllieName(String allieName){
        this.allieName = allieName;
        allieNameLabel.setText(allieName);

    }

    public void setThreadPool() {
        threadPool = new ThreadPoolExecutor(amountOfThreads, amountOfThreads, 20, TimeUnit.SECONDS, blockingQueue);
    }

    /**
     * This is what the agent supposed to do - Agent's Job.
     * He gets a string, remove all the forbidden characters from it - in case there is,
     * and encoding the result string.
     * If it looks like the origin possible string (The dictionary will decide it),
     * then he will Create MissionDTO - and insert it to blockingQueueResponses.
     */
    private class DecryptTask implements Runnable {
        private Engine engine;
        private Dictionary dictionary;
        private String excludedCharacters;
        private String toEncodeString;
        private int sizeOfMission;
        private ArrayList<String> initialPositions;
        private List<MissionDTO> candidatesList = new ArrayList<>();

        Consumer<Integer> consumer = amount -> updateProgressLabels(amount);


        public DecryptTask(Engine copyEngine, int sizeOfMission,
                           String toEncodeString, ArrayList<String> initialPositions){

            engine = copyEngine;

            this.sizeOfMission = sizeOfMission;
            this.toEncodeString = toEncodeString;
            this.dictionary = engine.getDictionary();
            this.excludedCharacters = dictionary.getExcludedCharacters();

            this.initialPositions = new ArrayList<>();
            for (String config : initialPositions) {
                this.initialPositions.add(config);
            }
        }

        /**In here the Agent should try to decode the String that the ThreadPool gave him.
         * Pay attention!
         * if mission's size is 4, then the Agent should do the WHOLE options.
         * for example, if the Rotors first positions is A,A,A ,
         * then the agent should do A,A,E , A,A,F, A,A,G , A,A,H - so each time he will start from different configuration,
         * in order to cover all possible cases.
         *
         */

        //the decryption here is without plugboard! therefor, we need to create a new method (not decodeStr),
        //or add boolean value inside this method.
        @Override
        public void run(){
            try {

                System.out.println("about to start a decrypt task");
                for (int i = 0; i < sizeOfMission; i++) {

                    boolean shouldContinueSearching = true;

                    String initPos = initialPositions.get(initialPositions.size() - 1 - i);
                    engine.initRotorsPositions(initPos);

                    long start = System.nanoTime();
                    DecodeStringInfo decodeStringInfo = engine.decodeStrWithoutPG(toEncodeString);

                    String resultString = decodeStringInfo.getDecodedString();

                    int numOfSeparates = getNumOfSeparates(resultString);
                    String[] resultWordsArr = resultString.split(" ", numOfSeparates + 1);

                    for (int t = 0; t < resultWordsArr.length && shouldContinueSearching; t++) {
                        if (!dictionary.isExistInDictionary(resultWordsArr[t])) {
                            shouldContinueSearching = false;
                        }
                    }

                    if(shouldContinueSearching){

                        Platform.runLater(() -> {
                            candidatesAmount.set(candidatesAmount.get() + 1);
                        });

                        MissionDTO missionDTO = new MissionDTO(agentMainAppController.getCurrentUserName(),
                                resultString, engine.getEngineFullDetails().getChosenReflector(),
                                initPos, engine.getEngineFullDetails().getUsedRotorsOrganization(),
                                allieName);
                        candidatesList.add(missionDTO);

                    }
//                    amountOfMissionsLeftInThreadPool.set(amountOfMissionsLeftInThreadPool.get() - 1);

                    amountOfDecoding++;
                    System.out.println("amount of time i decoded so far is: " + amountOfDecoding);
                }

                if(candidatesList.size() > 0) {
                    uploadCandidates(candidatesList);
                    updateFlowPane(candidatesList);
                }

                int update = amountOfCompletedMissionsSoFar.get() + 1;

                consumer.accept(update);

                //checking if the blocking queue is empty. if it is, we need to get more missions.
                if(blockingQueue.isEmpty()) {

                    System.out.println("about to poll missions");
                    pollMissions();

                    queueLock.notifyAll();
                }

            } catch (invalidInputException e) {
                System.out.println("exception in decrypt task");
            }
        }

        private int getNumOfSeparates(String string) {
            int numOfSeparates = 0;
            for (int i = 0; i < excludedCharacters.length(); i++) {//remove all excluded characters
                string = string.replace(String.valueOf(excludedCharacters.charAt(i)), "");
            }
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == ' ') {
                    numOfSeparates++;
                }
            }
            return numOfSeparates;
        }

    }

    private synchronized void updateProgressLabels(Integer amount) {
        Platform.runLater(() -> {
            amountOfCompletedMissionsSoFar.set(amount);
            amountOfMissionsInQueue.set(amountOfMissionsInQueue.get() - 1);
        });
    }

    private void uploadCandidates(List<MissionDTO> candidatesList) {

        String finalUrl = HttpUrl
                .parse(AGENT_POST_CANDIDATES_PATH)
                .newBuilder()
                .addQueryParameter("username", agentMainAppController.getCurrentUserName())
                .addQueryParameter("allyName", agentMainAppController.getAllieName())
                .build()
                .toString();

        String json = GSON_INSTANCE.toJson(candidatesList);

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(json.getBytes()))
                .build();

        Call call = new OkHttpClient().newCall(request);
        try {
            call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*try {
            Response response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/

    }

    private void updateFlowPane(List<MissionDTO> missionsList) {

        Platform.runLater(() -> {

            for (MissionDTO dto : missionsList) {

                if(dto != null){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    URL url = getClass().getResource(POTENTIALLY_DECRYPT_DATA_PAGE_FXML_RESOURCE_LOCATION);
                    fxmlLoader.setLocation(url);
                    try {
                        Parent root = fxmlLoader.load(url.openStream());
                        DecryptConfController controller = fxmlLoader.getController();

                        controller.setAgentLabel(dto.getAgentID());
                        controller.setDecodedToLabel(dto.getDecodedTo());
                        controller.setReflectorIDLabel(dto.getChosenReflector());
                        controller.setRotorsPositionsAndOrderLabel(dto.getRotorsPositionsAndOrder());

                        candidatesFP.getChildren().add(root);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    private void startIsContestEndRefresher(MissionDTO missionDTO) {
        Platform.runLater(() -> {
            isThereAWinner(missionDTO);
        });
    }

    private void isThereAWinner(MissionDTO missionDTO) {

        if (missionDTO != null) {
            if (missionDTO.isWinner()) {

                isActiveContest.set(false);
                contestEndTimer.cancel();

                threadPool.shutdownNow();
            }
        }
    }

    public void checkIfContestIsEnd() {
        contestEndRefresher = new IsContestEndRefresher(
                this::startIsContestEndRefresher, "",
                agentMainAppController.getCurrentUserName(),
                "Agent");
        contestEndTimer = new Timer();
        contestEndTimer.schedule(contestEndRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void getContestDetails(){
        String finalUrl = HttpUrl
                .parse(Constants.GET_CONTEST_DETAILS_FOR_AGENT_PATH)
                .newBuilder()
                .addQueryParameter("username", agentMainAppController.getCurrentUserName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
//                    errorMessageProperty.set("Error: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {

                    Platform.runLater(() ->
//                            errorMessageProperty.set("Error: " + responseBody)
                            popUpError(responseBody) //??
                    );
                } else {
                    Platform.runLater(() -> {
                        ContestDetailsForAgentDTO detailsDto = GSON_INSTANCE.fromJson(responseBody, ContestDetailsForAgentDTO.class);

                        battleFieldNameLabel.setText(detailsDto.getBattleName());
                        contestLevelLabel.setText(detailsDto.getDifficulty());
                        teamsLabel.setText(String.valueOf(detailsDto.getAmountOfTeams()));
                        contestStatusLabel.setText(detailsDto.getContestStatus());
                        allieNameLabel.setText(detailsDto.getAllyName());

                    });
                }
            }
        });
    }


    /*private AgentDTO agentDetailsRefresher() {
        Platform.runLater(() -> {
            return publishAgentDetails();
        });
    }*/

    private AgentDTO publishAgentDetails() {

        AgentDTO agentDTO = new AgentDTO(agentMainAppController.getCurrentUserName(), allieName,
                amountOfThreads, amountOfMissions, amountOfCompletedMissionsSoFar.get());

        return agentDTO;
    }

    public void publishAgent(){
        AgentDTO agentDTO = publishAgentDetails();

        String finalUrl = HttpUrl
                .parse(Constants.AGENT_POST_DETAILS_PATH)
                .newBuilder()
                .addQueryParameter("agentName", agentMainAppController.getCurrentUserName())
                .build()
                .toString();

        String json = GSON_INSTANCE.toJson(agentDTO, AgentDTO.class);

        HttpClientUtil.runAsyncPost(finalUrl, json, new Callback() { //logic is working. need to change the way it looks in the application.

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
//                        errorMessageProperty.set("Error: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            popUpError("Error: " + responseBody)
                    );
                }

            }
        });
    }
    public void startPublishAgentDetails() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActiveContest.get()) {
                    publishAgent();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        thread.start();
    }

    }


