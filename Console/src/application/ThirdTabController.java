package application;

import decryption.dm.DecryptionManager;
import dtos.DecodeStringInfo;
import dtos.DecryptionManagerDTO;
import dtos.MissionDTO;
import dtos.ProgressUpdateDTO;
import exceptions.invalidInputException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import logic.enigma.Dictionary;
import logic.enigma.Engine;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

import static decryption.dm.DecryptionManager.pauseLock;
import static decryption.dm.DecryptionManager.totalTimeResult;

public class ThirdTabController {
    private Engine tabThreeEngine;
    @FXML private TextField currentConfiguration;
    @FXML private Button processBtn;
    @FXML private Button resetBtn;
    @FXML private TextField encodeMsgTF;
    @FXML private TextField outputTF;
    @FXML private TextField searchInDictionaryTF;
    @FXML private Button clearBtn;
    @FXML private TextArea dictionaryTA;
    @FXML private Slider agentAmountSlider;
    @FXML private ChoiceBox<?> difficultyChoiceBox;
    @FXML private TextField taskSizeTF;
    @FXML private Button startBtn;
    @FXML private ToggleButton stopBtn;
    @FXML private ToggleButton pauseBtn;
    @FXML private ToggleButton resumeBtn;
    @FXML private FlowPane candidatesFP;
    @FXML private ProgressBar tasksProgressBar;
    @FXML private Label percentageLabel;
    @FXML private Label currAgentAmountLabel;
    @FXML private Label amountOfTasksLabel;
    @FXML private TextField amountOfTasksTF;
    @FXML private TextField totalTimeTF;
    @FXML private Label searchingSolutionsLabel;
    private MainPageController mainPageController;
    private int currAgentAmount;
    private int tasksSize;
    private double progressBarValue;
    private DecryptionManager decryptionManager;
    private Dictionary dictionary = null;
    private Consumer<MissionDTO> consumer = missionDTO -> showGoodDecryptedString(missionDTO);
    private Consumer<ProgressUpdateDTO> progressConsumer = progressUpdateDTO -> updateProgressBar(progressUpdateDTO);
    private BooleanProperty isSystemPause = new SimpleBooleanProperty();
    private BooleanProperty isStopBtnClicked = new SimpleBooleanProperty();

    @FXML public void initialize() {
        agentAmountSlider.valueProperty().addListener(new ChangeListener<Number>() {//set slider listener
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currAgentAmount = (int) agentAmountSlider.getValue();
                currAgentAmountLabel.setText(Integer.toString(currAgentAmount));
            }
        });
        isSystemPause.set(false);
        isStopBtnClicked.set(false);
        progressBarValue = 0;

        percentageLabel.setText(Double.toString(progressBarValue) + "%");

        ObservableList observableList = FXCollections.observableArrayList("Easy","Medium","Hard","Impossible");
        difficultyChoiceBox.setItems(observableList);
    }

    public void setAgentAmountSlider(int maxAgentSize) {
        agentAmountSlider.setMin(2);
        agentAmountSlider.setMax(maxAgentSize);
    }

    @FXML
    void encodeMsgListener(ActionEvent event) {

    }

    @FXML
    void pauseBtnListener(ActionEvent event) {
        isSystemPause.set(true);
 //       isPauseRunningTask();
        searchingSolutionsLabel.setVisible(false);
    }

    @FXML
    void clearFieldsListener(ActionEvent event) {
        encodeMsgTF.clear();
        outputTF.clear();
        searchInDictionaryTF.clear();
        dictionaryTA.clear();
    }

    @FXML
    void processBtnListener(ActionEvent event) {
        if(!encodeMsgTF.getText().equals("")){
            try {
                String inputString = encodeMsgTF.getText();
                isLegalString(inputString.toUpperCase());

                mainPageController.setDecodedCorrectly(false);

                DecodeStringInfo newInfo = tabThreeEngine.decodeStrWithoutPG(inputString);
                outputTF.setText(newInfo.getDecodedString());
                mainPageController.increaseDecodedStringAmount();//this is for the current configuration amount of decoded strings(output in statistics)
                mainPageController.setDecodedCorrectly(true);

                mainPageController.setAmountOfDecodedStrings(mainPageController.getAmountOfDecodedStrings() + 1);// update times of decode.

                String statisticNewString = "   " + mainPageController.getCurrConfigurationDecodedAmount() +
                        ". <" + newInfo.getToEncodeString() + "> ----> <" + newInfo.getDecodedString() + "> (" + newInfo.getTimeInMilli() + " nano seconds)\n";
                appendToStatistics(statisticNewString);

            } catch (invalidInputException e) {
                mainPageController.popUpError(e.getMessage());
            }
        }

    }

    private void appendToStatistics(String statisticNewString) {
        mainPageController.appendToStatistics(statisticNewString);
    }

    @FXML
    void resetBtnListener(ActionEvent event) {

        mainPageController.resetEngineToUserInitChoice();
        mainPageController.updateConfigurationLabel();
        clearTextFields();
    }

    private void clearTextFields() {
        encodeMsgTF.clear();
        searchInDictionaryTF.clear();
        dictionaryTA.clear();
        outputTF.setText("Reset has been made successfully!!");
        int x = 5;
    }

    @FXML
    void resumeBtnListener(ActionEvent event) {
        isSystemPause.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }

        searchingSolutionsLabel.setVisible(true);
    }

    @FXML
    void searchInDictionaryListener(KeyEvent event) {
        dictionaryTA.clear();
        String prefix = searchInDictionaryTF.getText().toUpperCase();
        ArrayList<String> wordsFromDictionary = dictionary.searchForSubStrings(prefix);

        for (int i = 0; i < wordsFromDictionary.size(); i++) {
            if(i == wordsFromDictionary.size() - 1){
                dictionaryTA.appendText(wordsFromDictionary.get(i));
            }else {
                dictionaryTA.appendText(wordsFromDictionary.get(i) + '\n');
            }
        }
    }

    @FXML
    void startBtnListener(ActionEvent event) {
        int agentAmount;
        String difficulty;

        try {
            agentAmount = (int)agentAmountSlider.getValue();
            difficulty = (String) difficultyChoiceBox.getValue();
            if(difficulty == null) {
                throw new invalidInputException("Please choose Difficulty.");
            }
            tasksSize = Integer.valueOf(taskSizeTF.getText());
            if(tasksSize < 1) { //maybe not a good condition.
                taskSizeTF.clear();
                throw new invalidInputException("Please choose size between 1 to 1000.");

            }
            //calculate amount of tasks here
            amountOfTasksTF.setText(taskSizeTF.getText()); // THIS IS TEMPORARY FOR NOW - NEED TO CHANGE
            //amountOfTasksTF.setVisible(true);
            amountOfTasksLabel.setVisible(true);
            searchingSolutionsLabel.setVisible(true);

            //disable 3 parameters that running
            agentAmountSlider.setDisable(true);
            difficultyChoiceBox.setDisable(true);
            taskSizeTF.setDisable(true);

            //opening options for stop, pause,resume
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            pauseBtn.setDisable(false);
            resumeBtn.setDisable(false);
            isStopBtnClicked.set(false);

            DecryptionManagerDTO decryptionManagerDTO = new DecryptionManagerDTO(agentAmount, tasksSize, outputTF.getText().toUpperCase(),
                    difficulty);
            decryptionManager = new DecryptionManager(decryptionManagerDTO, (Engine)tabThreeEngine.clone(),
                                                        consumer/*, progressConsumer*/,
                                                          isSystemPause, isStopBtnClicked,
                                                            totalTimeTF);

            decryptionManager.bindingsToUI(tasksProgressBar, percentageLabel);
            //start running tasks
            decryptionManager.encode();
        }
        catch (invalidInputException invalidEx) {
            mainPageController.popUpError(invalidEx.getMessage());
        }catch (RuntimeException noInputEx) {
            taskSizeTF.clear();
            mainPageController.popUpError("Please choose size(number) of each task.");
        }

        /*try {
            agentAmount = (int)agentAmountSlider.getValue();
        }catch (RuntimeException agentEx) {
            mainPageController.popUpError("Please choose amount of agents.");
        }
        try{
            difficulty = (String) difficultyChoiceBox.getValue();
        }catch (RuntimeException difficultyEx) {
            mainPageController.popUpError("Please choose Difficulty.");
        }
        try {
            tasksSize = Integer.valueOf(taskSizeTF.getText());
            if(tasksSize < 1)
                throw new invalidInputException("Please choose size between 0 to 1000.");
        }catch (RuntimeException tasksEx) {
            mainPageController.popUpError("Please choose size of each task.");
        } catch (invalidInputException e) {
            mainPageController.popUpError(e.getMessage());
        }
        System.out.println("good");*/

    }

    @FXML
    void stopBtnListener(ActionEvent event) {

        decryptionManager.stopAllAgents(); //shutdown all threads.

        isStopBtnClicked.set(true);
        agentAmountSlider.setDisable(false);
        difficultyChoiceBox.setDisable(false);
        difficultyChoiceBox.setValue(null);
        taskSizeTF.setDisable(false);
        taskSizeTF.clear();
        startBtn.setDisable(false);

        searchingSolutionsLabel.setVisible(false);
        amountOfTasksLabel.setVisible(false);
        amountOfTasksTF.setVisible(false);
        totalTimeResult();
        //progressBarValue = 0;


        unbind();


        tasksProgressBar.setProgress(0);
        percentageLabel.setText(Double.toString(progressBarValue) + "%");

        candidatesFP.getChildren().clear();
    }

    @FXML
    void taskSizeListener(ActionEvent event) {
        tasksSize = Integer.valueOf(taskSizeTF.getText());
        taskSizeTF.setDisable(true);
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setCurrentConfigurationTF(String currConfiguration) {
        currentConfiguration.setText(currConfiguration);
    }

    public void updateProgressBar(ProgressUpdateDTO progressUpdateDTO) { //need to make changes!! - now not working.
        //....
        //update progress bar by the overall tasks amount and then
        progressBarValue = progressUpdateDTO.getProgress() / progressUpdateDTO.getAllPossibleMissions();

        tasksProgressBar.setProgress(progressBarValue);


//        percentageLabel.setText((progressBarValue * 100) + "%");
    }

    public void DisableAllButtonsAndTextFields() {
        processBtn.setDisable(true);
        resetBtn.setDisable(true);
        clearBtn.setDisable(true);
        encodeMsgTF.setDisable(true);
        searchInDictionaryTF.setDisable(true);
        stopBtn.setDisable(true);
        pauseBtn.setDisable(true);
        resumeBtn.setDisable(true);
        startBtn.setDisable(true);
        amountOfTasksLabel.setVisible(false);
        amountOfTasksTF.setVisible(false);
    }

    public void setDecodingButtonsDisable(boolean setToDisable) {
        processBtn.setDisable(setToDisable);
        resetBtn.setDisable(setToDisable);
        clearBtn.setDisable(setToDisable);
        encodeMsgTF.setDisable(setToDisable);
        searchInDictionaryTF.setDisable(setToDisable);
        startBtn.setDisable(setToDisable);

    }

    public void showGoodDecryptedString(MissionDTO missionDTO){
        Platform.runLater(() -> {
            if(missionDTO != null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("../resources/Configuration.fxml");
                fxmlLoader.setLocation(url);
                try {
                    Parent root = fxmlLoader.load(url.openStream());
                    ConfigurationController configurationController = fxmlLoader.getController();

//                    configurationController.setConfigurationLabel(missionDTO.getConfiguration());
                    configurationController.setDecodedToLabel(missionDTO.getDecodedTo());
                    configurationController.setReflectorIDLabel(missionDTO.getChosenReflector());
                    configurationController.setRotorsOrderLabel(missionDTO.getRotorsOrder());
                    configurationController.setToEncodeLabel(missionDTO.getToEncodeString());
                    configurationController.setAgentIDLabel(missionDTO.getAgentID());
                    configurationController.setRotorsPositionsLabel(missionDTO.getRotorsPosition());
                    configurationController.setTimeTakenLabel(String.valueOf(missionDTO.getTimeTaken()));

                    candidatesFP.getChildren().add(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setEngine(Engine engine) {
        tabThreeEngine = engine;
    }

    public void unbind(){
        tasksProgressBar.progressProperty().unbind();
        percentageLabel.textProperty().unbind();
    }

    public void prepareTab() {
        unbind();

        currentConfiguration.clear();
        encodeMsgTF.clear();
        outputTF.clear();
        dictionaryTA.clear();
        searchInDictionaryTF.clear();
        candidatesFP.getChildren().clear();
        difficultyChoiceBox.setValue(null);
        taskSizeTF.clear();

        totalTimeTF.setText("0 Sec");

        tasksProgressBar.setProgress(0);
        percentageLabel.setText("0%");
    }

    public boolean isLegalString(String stringFromUser) throws invalidInputException {

        int numOfSeparates = getNumOfSeparates(stringFromUser);

        String[] wordsArr = stringFromUser.split(" ", numOfSeparates + 1);
        for (String string : wordsArr) {
            if(!dictionary.isExistInDictionary(string)){
                throw new invalidInputException("The string " + string + " isn't in the dictionary!");
            }
        }
        return true;
    }

    private int getNumOfSeparates(String string) {
        int numOfSeperates = 0;
        for (int i = 0; i < dictionary.getExcludedCharacters().length(); i++) {//remove all excluded characters
            string = string.replace(String.valueOf(dictionary.getExcludedCharacters().charAt(i)), "");
        }
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                numOfSeperates++;
            }
        }
        return numOfSeperates;
    }

}