import exceptions.invalidInputException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.*;

import java.net.URL;
import java.util.Arrays;

import static util.Constants.UBOAT_MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class UBoatApplication extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(UBOAT_MAIN_PAGE_FXML_RESOURCE_LOCATION);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root, 500, 620);
        primaryStage.setTitle("UBoat App");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }


    /*public static void main(String[] args) {
        String BASE_URL = "http://localhost:8080";
        OkHttpClient HTTP_CLIENT = new OkHttpClient();

        String ADD_BATTLEFIELD_RESOURCE = "/battleFieldList/new";
        String GET_BATTLEFIELD_RESOURCE = "/battleFieldList";

        String battleFieldNameFromUser = "Ran";
        String[] guests = {"OFEK", "RAN", "HELLO"};

        *//*Arrays.stream(guests).forEach(guest -> {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/UBoat" + ADD_BATTLEFIELD_RESOURCE).newBuilder();
            urlBuilder.addQueryParameter("battleField", guest);
            String finalUrl = urlBuilder.build().toString();
            System.out.println("About to add guest " + guest + "(" + finalUrl + ")");

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .put(RequestBody.create(new byte[]{}))
                    .build();

            Call call = HTTP_CLIENT.newCall(request);

            try {
                Response response = call.execute();
                System.out.println(response.body().string());
            } catch (IOException e) {
                System.out.println("Error when adding guest " + guest + ". Exception: " + e.getMessage());
            }

        });*//*

        Request request = new Request.Builder()
                .url(BASE_URL  + "/UBoat" + GET_BATTLEFIELD_RESOURCE)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        int amountOfComma = 0;
        try {
            Response response = call.execute();
//            System.out.println(response.body().string());

            String responseBody = response.body().string();
            responseBody = responseBody.trim();
            for (int i = 0; i < responseBody.length(); i++) {
                if(responseBody.charAt(i) == ','){
                    amountOfComma++;
                }
            }
            String[] bfNames = responseBody.
                                        split(",", amountOfComma + 1);

            for (int i = 0; i < bfNames.length; i++) {
                if(bfNames[i].equals(battleFieldNameFromUser)){
                    System.out.println("the name: " + battleFieldNameFromUser + " is already taken.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error when trying to get guests list. Exception: " + e.getMessage());
        }


        //if we got here, the battlefield name isn't taken
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/UBoat" + ADD_BATTLEFIELD_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("battleField", battleFieldNameFromUser);
        String finalUrl = urlBuilder.build().toString();
        System.out.println("About to add guest " + battleFieldNameFromUser + "(" + finalUrl + ")");

        request = new Request.Builder()
                .url(finalUrl)
                .put(RequestBody.create(new byte[]{}))
                .build();

        call = HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println("Error when adding guest " + battleFieldNameFromUser + ". Exception: " + e.getMessage());
        }


    }*/
}
