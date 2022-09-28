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
    @FXML private Button stopBtn;
    @FXML private Button pauseBtn;
    @FXML private Button resumeBtn;
    @FXML private FlowPane candidatesFP;
    @FXML private ProgressBar tasksProgressBar;
    @FXML private Label percentageLabel;
    @FXML private Label currAgentAmountLabel;
    @FXML private Label amountOfTasksLabel;
    @FXML private TextField amountOfTasksTF;
    @FXML private TextField totalTimeTF;
    @FXML private Label searchingSolutionsLabel;
    @FXML private Label searchStoppedLabel;
    private MainPageController mainPageController;
    private int currAgentAmount;
    private int tasksSize;
    private double progressBarValue;
    private DecryptionManager decryptionManager;
    private Dictionary dictionary = null;
    private Consumer<MissionDTO> consumer = missionDTO -> showGoodDecryptedString(missionDTO);

    //private Consumer<ProgressUpdateDTO> progressConsumer = progressUpdateDTO -> updateProgressBar(progressUpdateDTO);
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
        processBtnListener(event);
    }

    @FXML
    void pauseBtnListener(ActionEvent event) {
        isSystemPause.set(true);
 //       isPauseRunningTask();
        searchingSolutionsLabel.setVisible(false);

        searchStoppedLabel.setVisible(true);
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

    }

    @FXML
    void resumeBtnListener(ActionEvent event) {
        isSystemPause.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }

        searchStoppedLabel.setVisible(false);
        searchingSolutionsLabel.setVisible(true);
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
            if(tasksSize < 1 ) { //maybe not a good condition.
                taskSizeTF.clear();
                throw new invalidInputException("Please choose positive size.");

            }
            if(encodeMsgTF.getText().equals("") || outputTF.getText().equals("Reset has been made successfully!!")){
                encodeMsgTF.clear();
                outputTF.clear();
                throw new invalidInputException("Please choose string to decode.");
            }


            //disable 3 parameters that running
            agentAmountSlider.setDisable(true);
            difficultyChoiceBox.setDisable(true);
            taskSizeTF.setDisable(true);
            searchingSolutionsLabel.setVisible(true);

            //opening options for stop, pause,resume
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            pauseBtn.setDisable(false);
            resumeBtn.setDisable(false);
            isStopBtnClicked.set(false);

            DecryptionManagerDTO decryptionManagerDTO = new DecryptionManagerDTO(agentAmount, tasksSize, outputTF.getText().toUpperCase(), difficulty);
            decryptionManager = new DecryptionManager(decryptionManagerDTO, (Engine)tabThreeEngine.clone(),
                                                        consumer/*, progressConsumer*/,
                                                          isSystemPause, isStopBtnClicked,
                                                            totalTimeTF);

            //calculate amount of tasks here
            amountOfTasksLabel.setVisible(true);
            amountOfTasksTF.setText(String.valueOf(decryptionManager.getAmountOfMissions()));
            amountOfTasksTF.setVisible(true);


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

    }

    @FXML
    void stopBtnListener(ActionEvent event) {

        decryptionManager.stopAllAgents(); //shutdown all threads.

        totalTimeTF.clear();
        amountOfTasksLabel.setVisible(false);
        amountOfTasksTF.clear();
        amountOfTasksTF.setVisible(false);

        isStopBtnClicked.set(true);
        agentAmountSlider.setDisable(false);
        difficultyChoiceBox.setDisable(false);
        difficultyChoiceBox.setValue(null);
        taskSizeTF.setDisable(false);
        taskSizeTF.clear();
        startBtn.setDisable(false);

        searchingSolutionsLabel.setVisible(false);
        searchStoppedLabel.setVisible(false);
        amountOfTasksLabel.setVisible(false);
        amountOfTasksTF.setVisible(false);
        totalTimeResult();//why is this here

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

    /*public void updateProgressBar(ProgressUpdateDTO progressUpdateDTO) { //need to make changes!! - now not working.
        //....
        //update progress bar by the overall tasks amount and then
        progressBarValue = progressUpdateDTO.getProgress() / progressUpdateDTO.getAllPossibleMissions();

        tasksProgressBar.setProgress(progressBarValue);

        if(tasksProgressBar.getProgress() == 100)
            searchingSolutionsLabel.setVisible(false);

    }*/

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

        agentAmountSlider.setDisable(false);
        difficultyChoiceBox.setDisable(false);
        taskSizeTF.setDisable(false);
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
                URL url = getClass().getResource("/resources/Configuration.fxml");
                fxmlLoader.setLocation(url);
                try {
                    Parent root = fxmlLoader.load(url.openStream());
                    ConfigurationController configurationController = fxmlLoader.getController();

                    configurationController.setDecodedToLabel(missionDTO.getDecodedTo());
                    configurationController.setReflectorIDLabel(missionDTO.getChosenReflector());
                    configurationController.setRotorsPositionsAndOrderLabel(missionDTO.getRotorsPositionsAndOrder());

                    configurationController.setAgentIDLabel(missionDTO.getAgentID());


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