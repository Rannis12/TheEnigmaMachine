import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class UBoatApplication extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/resources/MainPage.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());


        Scene scene = new Scene(root, 500, 620);
        primaryStage.setTitle("UBoat");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(500);
        primaryStage.show();
    }


    public static void main(String[] args) {
        String BASE_URL = "http://localhost:8080";
        OkHttpClient HTTP_CLIENT = new OkHttpClient();

        String ADD_BATTLEFIELD_RESOURCE = "/battleFieldList/new";
        String GET_BATTLEFIELD_RESOURCE = "/battleFieldList";

        String[] guests = {"OFEK", "RAN", "HELLO"};

        Arrays.stream(guests).forEach(guest -> {
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

        });

        Request request = new Request.Builder()
                .url(BASE_URL  + "/UBoat" + GET_BATTLEFIELD_RESOURCE)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();


            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println("Error when trying to get guests list. Exception: " + e.getMessage());
        }

    }

}
