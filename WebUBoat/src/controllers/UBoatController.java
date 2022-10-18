package controllers;

import com.google.gson.Gson;
import dtos.*;
import exceptions.invalidInputException;
import exceptions.invalidXMLfileException;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.enigma.Engine;
import logic.enigma.EngineLoader;
import okhttp3.*;
import util.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static logic.enigma.Engine.makeCodeForm;
import static util.Constants.RANDOM_CONFIGURATION;

public class UBoatController {
    private UBoatMainAppController uBoatMainAppController;
    public static String BASE_URL = "http://localhost:8080";
    public static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    @FXML private FirstTabController firstTabController;
    @FXML private SecondTabController secondTabController;
    @FXML private Label xmlPathLabel;
    @FXML private Label userLabel;
    private Engine engine;
    private int currConfigurationDecodedAmount;
    private BooleanProperty decodedCorrectly = new SimpleBooleanProperty();
    private IntegerProperty amountOfDecodedStrings = new SimpleIntegerProperty();

    private StringProperty configuration = new SimpleStringProperty();

    @FXML public void initialize() {
        if(firstTabController != null && secondTabController != null /*&& thirdTabController != null*/){
            firstTabController.setMainController(this);
            secondTabController.setMainPageController(this);
        }


    }
    @FXML
    void loadFile(MouseEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            if(loadFileFromXml(file.getAbsolutePath())) {
                setAgentAmountSlider();
                firstTabController.showDetails(engine.getEngineMinimalDetails());
                firstTabController.enableButtons();
                firstTabController.clearConfigurationTextFields();

                secondTabController.disableAllButtonsAndTextFields();
                secondTabController.clearCurrentConfigurationTA();

                /*thirdTabController.DisableAllButtonsAndTextFields();*/

                amountOfDecodedStrings.unbind();
                configuration.unbind();

                bindingsToConfigurationsControllers();

                amountOfDecodedStrings = new SimpleIntegerProperty(0);

                uploadFileToServer(file);

            }
        }
    }

    private void bindingsToConfigurationsControllers() {
        firstTabController.getConfigurationTF().textProperty().bind(configuration);
        secondTabController.getConfigurationTF().textProperty().bind(configuration);

    }

    /**
     * this method uploads game settings (xml file) to server
     * @param file - xml file that represents game settings.
     */
    private void uploadFileToServer(File file) throws IOException {
        String RESOURCE = "/upload-file";

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                        .build();

        String finalUrl = HttpUrl
                .parse(BASE_URL + RESOURCE)
                .newBuilder()
                .addQueryParameter("username", uBoatMainAppController.getUserName())
                .build()
                .toString();


        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();


        //getting response body from json (includes machine minimal details).

        EngineMinimalDetailsDTO detailsFromJson = Constants.GSON_INSTANCE
                                                            .fromJson(response.body().string(), EngineMinimalDetailsDTO.class);

        firstTabController.showDetails(detailsFromJson);
    }


    private void setAgentAmountSlider() {
        int agentMaxAmount = engine.getAgentMaxAmount();
        /*thirdTabController.setAgentAmountSlider(agentMaxAmount);*/
    }

    public void updateConfigurationLabel() {
        EngineFullDetailsDTO engineFullDetailsDTO = getEngineFullDetails();
        String newConfiguration = makeCodeForm(engineFullDetailsDTO.getNotchesCurrentPlaces(), engineFullDetailsDTO.getUsedRotorsOrganization(),
                engineFullDetailsDTO.getRotorsCurrentPositions(), engineFullDetailsDTO.getChosenReflector());

        firstTabController.setCurrentConfigurationTF(newConfiguration);
        secondTabController.setCurrentConfigurationTF(newConfiguration);
    }
    public boolean loadFileFromXml(String fileDestination){
        try {

            EngineLoader engineLoader = new EngineLoader(fileDestination);
            engine = engineLoader.loadEngineFromXml(fileDestination); //Ran added. updates the dictionary in thirdController.

            firstTabController.setReflectorsCB(getReflectorsIDs());

            engine.resetStatistics();

            xmlPathLabel.setText(fileDestination + " selected");
            secondTabController.setDecodingButtonsDisable(true);
            /*thirdTabController.setDictionary(engine.getDictionary());
            thirdTabController.setEngine(engine);
            thirdTabController.prepareTab();*/
            return true;

        } catch (invalidXMLfileException e) {
            popUpError(e.getMessage());
        }
        return false;
    }
    public void randomConfiguration() {

        String finalUrl = HttpUrl
                .parse(RANDOM_CONFIGURATION)
                .newBuilder()
                .addQueryParameter("username", uBoatMainAppController.getUserName())
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(new byte[]{}))
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();
            configuration.set(response.body().string());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public EngineFullDetailsDTO getEngineFullDetails(){
        return engine.getEngineFullDetails();
    }


    public void setTabsConfiguration(String newConfiguration) {
        configuration.set(newConfiguration);
//        firstTabController.setCurrentConfigurationTF(newConfiguration);
//        secondTabController.setCurrentConfigurationTF(newConfiguration);
        /*thirdTabController.setCurrentConfigurationTF(newConfiguration);*/

//        secondTabController.getStatisticsTA().appendText(newConfiguration + '\n');
    }

    public void enableEncryptFunction() {
        secondTabController.enableEncryptFunction();
    }
    public int getUsedAmountOfRotors() {
        return engine.getEngineMinimalDetails().getUsedAmountOfRotors();
    }
    public void checkRotorIndexesValidity(String rotorsPosition, RotorsIndexesDTO rotorsIndexesDTO) throws invalidInputException {
        engine.checkRotorIndexesValidity(rotorsPosition, rotorsIndexesDTO.getUIRotorsIndexes());
    }
    public void checkRotorsFirstPositionsValidity(String rotorsFirstPositions) throws invalidInputException {
        engine.checkRotorsFirstPositionsValidity(rotorsFirstPositions, engine.getKeyBoard());
    }
    public void checkPlugBoardValidity(String tmpString, PlugBoardDTO plugBoardDTO) throws invalidInputException {
        engine.checkPlugBoardValidity(tmpString, plugBoardDTO.getUICables());
    }
    public void setNewMachine(RotorsFirstPositionDTO rotorsFirstPositionDTO, PlugBoardDTO plugBoardDTO, ReflectorDTO reflectorDTO, RotorsIndexesDTO rotorsIndexesDTO) {
        engine.setNewMachine(rotorsFirstPositionDTO, plugBoardDTO, reflectorDTO, rotorsIndexesDTO);
    }
    public int getReflectorsIDs(){
        return engine.getReflectorsIDs();
    }
    public void popUpError(String errorMsg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid input");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }
    public DecodeStringInfo decodeString(String text) throws invalidInputException {
        DecodeStringInfo decodeStringInfo = engine.decodeStr(text);
        return decodeStringInfo;
    }
    public char decodeChar(char character){
        return engine.decodeChar(character);
    }
    public void increaseDecodedStringAmount(){
        currConfigurationDecodedAmount++;
    }
    public int getCurrConfigurationDecodedAmount() {
        return currConfigurationDecodedAmount;
    }
    public void resetEngineToUserInitChoice() {
        engine.resetEngineToUserInitChoice();
    }
    public void setDecodedCorrectly(boolean value) {
        decodedCorrectly.set(value);
    }
 /*   public void appendToStatistics(String statisticNewString) {
        secondTabController.appendToStatistics(statisticNewString);
    }*/
    public int getAmountOfDecodedStrings() {
        return amountOfDecodedStrings.get();
    }
    public void setAmountOfDecodedStrings(int amountOfDecodedStrings) {
        this.amountOfDecodedStrings.set(amountOfDecodedStrings);
    }

    public void setuBoatMainAppController(UBoatMainAppController uBoatMainAppController) {
        this.uBoatMainAppController = uBoatMainAppController;
    }

    public Label getUserLabel() {
        return userLabel;
    }

    public String getUserName(){
        return uBoatMainAppController.getUserName();
    }

    public void resetCurrConfigurationDecodedAmount() {
        currConfigurationDecodedAmount = 0;
    }
}

