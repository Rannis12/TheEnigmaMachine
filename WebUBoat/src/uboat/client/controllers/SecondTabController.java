package uboat.client.controllers;

//import client.http.HttpClientUtil;
import allie.client.controllers.TeamDetailsController;
import client.http.HttpClientUtil;
import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import dtos.DecodedStringAndConfigurationDTO;
import dtos.TeamInformationDTO;
import dtos.engine.DictionaryDTO;
import dtos.web.ContestDetailsDTO;
import exceptions.invalidInputException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import logic.BattleFieldRefresher;
import logic.TeamInformationRefresher;
import logic.enigma.Dictionary;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static utils.Constants.*;
import static utils.Constants.GSON_INSTANCE;


public class SecondTabController {

    private UBoatController uBoatController;
    private boolean shouldDecodeLine; //false = should decode char

    @FXML
    private TextField currentConfiguration;
    @FXML
    private TextField lineInputTF;
    @FXML
    private Button readyBtn;
    @FXML
    private TextField decodeResultTF;
    @FXML private TextField searchInDictionaryTF;
    @FXML private TextArea dictionaryTA;
    @FXML
    private Button clearBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button EncryptBtn;
    @FXML
    private FlowPane teamsFP;
    @FXML
    private Button logoutBtn;

    private BooleanProperty isUBoatReady = new SimpleBooleanProperty(false);
    private BooleanProperty isStringDecrypted = new SimpleBooleanProperty(false);
    private Timer timer;
    private TeamInformationRefresher battleFieldListRefresher;
    private String battleName;
    private Dictionary dictionary = null;

    @FXML
    public void initialize() {
//        isUBoatReady.set(false);
        readyBtn.setTextFill(Color.RED);
        isUBoatReady.addListener(e -> {
            if (isUBoatReady.getValue()) {
                readyBtn.setTextFill(Color.GREEN);
                EncryptBtn.setDisable(true);
                resetBtn.setDisable(true);

            } else {
                readyBtn.setTextFill(Color.RED);
                EncryptBtn.setDisable(false);
                resetBtn.setDisable(false);

            }
        });

        isStringDecrypted.addListener(e -> {
            if (isStringDecrypted.getValue()) {
                readyBtn.setDisable(false);
            } else {
                readyBtn.setDisable(true);
            }
        });
    }


    @FXML
    void EncryptBtnListener(ActionEvent event) { //need to fix it tomorrow. look at ContestController - servlet isReadyServlet.
        uBoatController.setDecodedCorrectly(false);
        String toDecode = lineInputTF.getText().toUpperCase();

        if (!toDecode.equals("")) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(DECODE_STRING_PAGE).newBuilder();
            urlBuilder.addQueryParameter("toDecode", toDecode)
                    .addQueryParameter("username", uBoatController.getUserName());

            String finalUrl = urlBuilder.build().toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Error: " + e.getMessage());
                        uBoatController.popUpError(e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                uBoatController.popUpError("Error: " + responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            Gson gson = new Gson();
                            DecodedStringAndConfigurationDTO dto = gson.fromJson(responseBody, DecodedStringAndConfigurationDTO.class);

                            decodeResultTF.setText(dto.getDecodedString());
                            uBoatController.setTabsConfiguration(dto.getCurrConfiguration());

                            uBoatController.setDecodedCorrectly(true);
                            isStringDecrypted.set(true);
                        });
                    }
                }
            });
        }
    }

    @FXML
    void clearBtnListener(ActionEvent event) {
        clearEncryptTextFields();
        isStringDecrypted.set(false);
    }

    @FXML
    void resetBtnListener(ActionEvent event) throws InterruptedException {

        String finalUrl = HttpUrl
                .parse(FULL_SERVER_PATH + "/reset-engine")
                .newBuilder()
                .addQueryParameter("username", uBoatController.getUserName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);

        try {
            Response response = call.execute();
            String conf = response.body().string();
            conf = conf.trim();

            uBoatController.setTabsConfiguration(conf);


        } catch (IOException e) {
            throw new RuntimeException("error in reset the engine");
        }

//        UBoatController.updateConfigurationLabel();/*instead of this we can do ++ and then -- to
//        the integerProperty and the listener will update automatically, but it's weird*/

        clearEncryptTextFields();
        isStringDecrypted.set(false);

        decodeResultTF.setText("Reset has been made successfully!!");
    }

    @FXML
    void logoutBtnListener(ActionEvent event) {

    }

    @FXML
    void readyBtnListener(ActionEvent event) {

        isUBoatReady.set(!isUBoatReady.getValue());
        String status = fromBooleanToString(isUBoatReady.getValue());

        String finalUrl = HttpUrl
                .parse(UBOAT_POST_HIS_STATUS_PATH)
                .newBuilder()
                .addQueryParameter("username",uBoatController.getUserName())
                .addQueryParameter("status", status)
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getMessage());
                    uBoatController.popUpError(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            uBoatController.popUpError("Error: " + responseBody)
                    );
                } //else {
                //Platform.runLater(() -> {
                //  uBoatMainAppController.updateUserName(userName);
                //uBoatMainAppController.switchToUBoatRoom();
                //});
                // }
            }
        });

        startTeamInformationRefresher(); //maybe won't work
    }

    @FXML
    void searchInDictionaryListener(KeyEvent event) {
        dictionaryTA.clear();
        String prefix = searchInDictionaryTF.getText().toUpperCase();
        ArrayList<String> wordsFromDictionary = dictionary.searchForSubStrings(prefix);

        for (int i = 1; i <= wordsFromDictionary.size(); i++) {
            if(i == wordsFromDictionary.size())
                dictionaryTA.appendText(wordsFromDictionary.get(i-1));
            else if((i % 2) ==0 )
                dictionaryTA.appendText(wordsFromDictionary.get(i-1) + '\n');
            else
                dictionaryTA.appendText(wordsFromDictionary.get(i-1) + "\t\t\t");

        }
    }

    private String fromBooleanToString(Boolean value) {
        if(value){
            return "Ready";
        }
        return "Not Ready";
    }

    public void setMainPageController(UBoatController uBoatController) {
        this.uBoatController = uBoatController;
    }

    public void setCurrentConfigurationTF(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }

    public void setDecodingButtonsDisable(boolean setToDisable) {
        resetBtn.setDisable(setToDisable);
    }

    public void clearEncryptTextFields(){
//        charInputTF.clear();
        lineInputTF.clear();
        decodeResultTF.clear();
    }

    public void clearCurrentConfigurationTA() {
        currentConfiguration.clear();
    }
    public void enableEncryptFunction() {
        lineInputTF.setDisable(false);
        EncryptBtn.setDisable(false);
        resetBtn.setDisable(false);
    }

    public void disableAllButtonsAndTextFields() {
//        decodeCharBtn.setDisable(true);
//        charInputTF.setDisable(true);
//        doneBtn.setDisable(true);
        lineInputTF.setDisable(true);
//        processBtn.setDisable(true);
        clearBtn.setDisable(true);
        searchInDictionaryTF.setDisable(false);
    }

    public TextField getConfigurationTF() {
        return currentConfiguration;
    }


    public void startTeamInformationRefresher() {
        battleFieldListRefresher = new TeamInformationRefresher(
                this::updateTeamFlowPane,
                battleName);
        timer = new Timer();
        timer.schedule(battleFieldListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void showTeamInformationDetails(List<TeamInformationDTO> dtosList) {

        teamsFP.getChildren().clear();
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

                    teamsFP.getChildren().add(root);

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

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public void setDictionary(DictionaryDTO dictionaryDTO) {
        this.dictionary = new Dictionary(dictionaryDTO.getDictionary(), dictionaryDTO.getExcludedCharacters());
    }


    /*public void appendToStatistics(String statisticNewString) {
        statisticsTA.appendText(statisticNewString);
    }*/

/*    @FXML
    void doneBtnListener(ActionEvent event) {
        mainPageController.setDecodedCorrectly(false);
        if(!charInputTF.getText().equals("")) {
            mainPageController.setAmountOfDecodedStrings(mainPageController.getAmountOfDecodedStrings() + 1);

            mainPageController.setDecodedCorrectly(true);

            mainPageController.increaseDecodedStringAmount();
            appendToStatistics("   " + mainPageController.getCurrConfigurationDecodedAmount() +
                    ". <" + charInputTF.getText().toUpperCase() + "> ----> <" + decodeResultTF.getText() + "> ( NO nano seconds)\n");
        }

        clearEncryptTextFields();

    }*/

/*    @FXML
    void charInputListener(KeyEvent event) {
        char nextChar = 0;
        if(!event.getText().equals("")) {
            try {
                nextChar = event.getText().toUpperCase().charAt(0);
                decodeResultTF.setText(decodeResultTF.getText() + mainPageController.decodeChar(nextChar));
            }catch (RuntimeException ex) {//edge scenario - input from user is invalid so pop up msg
                mainPageController.popUpError("The letter " + nextChar + " doesn't exist in the language. Please try again.\n");
            }
        }
    }*/

/*    @FXML
    void decodeCharListener(ActionEvent event) {
        clearEncryptTextFields();

        lineInputTF.setDisable(true);
//        processBtn.setDisable(true);
        clearBtn.setDisable(true);

//        doneBtn.setDisable(false);

//        charInputTF.setDisable(!lineInputTF.isDisable());

        shouldDecodeLine = false;
    }*/
    /*    public TextArea getStatisticsTA(){
        return statisticsTA;
    }*/
}
