package login;

import client.http.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import controllers.AlliesMainAppController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;

public class LoginController {

    @FXML private TextField nameTF;
    @FXML private Label errorMessageLabel;
    private AlliesMainAppController alliesMainAppController;

    private StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize(){
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }
    @FXML
    void loginBtnListener(ActionEvent event) {

        String userName = nameTF.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse("http://localhost:8080/loginShortResponse")
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("type", "UBoat")
                .build()
                .toString();

//        updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() { //logic is working. need to change the way it looks in the application.

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Something went wrong: " + e.getMessage());
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {

                    Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
//                        uBoatMainAppController.updateUserName(userName);
//                        uBoatMainAppController.switchToUBoatRoom();
                    });
                }
            }
        });
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.alliesMainAppController = alliesMainAppController;
    }


}
